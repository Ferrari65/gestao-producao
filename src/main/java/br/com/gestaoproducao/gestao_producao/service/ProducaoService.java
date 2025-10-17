package br.com.gestaoproducao.gestao_producao.service;

import br.com.gestaoproducao.gestao_producao.domain.CustoProducao;
import br.com.gestaoproducao.gestao_producao.domain.ProducaoDiaria;
import br.com.gestaoproducao.gestao_producao.domain.Produto;
import br.com.gestaoproducao.gestao_producao.dto.*;
import br.com.gestaoproducao.gestao_producao.repositories.CustoProducaoRepository;
import br.com.gestaoproducao.gestao_producao.repositories.ProducaoDiariaRepository;
import br.com.gestaoproducao.gestao_producao.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProducaoService {
    private final ProdutoRepository produtoRepo;
    private final ProducaoDiariaRepository producaoRepo;
    private final CustoProducaoRepository custoRepo;

    @Transactional
    public Produto criarProduto(ProdutoRequest req){
        if (produtoRepo.existsByNomeIgnoreCase(req.nome()))
            throw new IllegalArgumentException("Produto já existe: " + req.nome());
        var p = Produto.builder().nome(req.nome()).unidadeVenda(req.unidadeVenda()).precoVenda(req.precoVenda()).build();
        return produtoRepo.save(p);
    }

    @Transactional
    public CustoProducao adicionarCusto(CustoProducaoRequest req){
        var produto = produtoRepo.findById(req.idProduto()).orElseThrow();
        var c = CustoProducao.builder()
                .produto(produto).nomeInsumo(req.nomeInsumo())
                .custoTotalCompra(req.custoTotalCompra())
                .quantidadeTotalCompra(req.quantidadeTotalCompra())
                .unidadeBase(req.unidadeBase())
                .consumoPorUnidade(req.consumoPorUnidade())
                .vigenteDe(req.vigenteDe()).vigenteAte(req.vigenteAte())
                .build();
        return custoRepo.save(c);
    }

    @Transactional
    public ProducaoDiaria lancarProducao(ProducaoDiariaRequest req){
        var produto = produtoRepo.findById(req.idProduto()).orElseThrow();
        var jaExiste = producaoRepo.findDoProdutoNoPeriodo(produto.getIdProduto(), req.data(), req.data().plusDays(1));
        if(!jaExiste.isEmpty()) throw new IllegalArgumentException("Já existe lançamento para este produto nesta data.");
        var l = ProducaoDiaria.builder().produto(produto).data(req.data()).quantidade(req.quantidade()).observacao(req.observacao()).build();
        return producaoRepo.save(l);
    }

    public RelatorioMensalConsolidadoDTO relatorioConsolidado(int ano, int mes){
        var periodo = YearMonth.of(ano, mes);
        var inicio = periodo.atDay(1); var fim = periodo.plusMonths(1).atDay(1);
        var lancs = producaoRepo.findNoPeriodo(inicio, fim);

        BigDecimal receita = BigDecimal.ZERO, custo = BigDecimal.ZERO;
        for (var l: lancs){
            var preco = l.getProduto().getPrecoVenda();
            receita = receita.add(preco.multiply(BigDecimal.valueOf(l.getQuantidade())));
            var custoUnit = calcularCustoUnitario(l.getProduto().getIdProduto(), l.getData());
            custo = custo.add(custoUnit.multiply(BigDecimal.valueOf(l.getQuantidade())));
        }
        var lucro = receita.subtract(custo);
        return new RelatorioMensalConsolidadoDTO(ano, mes,
                receita.setScale(2, RoundingMode.HALF_UP),
                custo.setScale(2, RoundingMode.HALF_UP),
                lucro.setScale(2, RoundingMode.HALF_UP));
    }

    public List<RelatorioMensalPorProdutoDTO> relatorioPorProduto(int ano, int mes){
        var periodo = YearMonth.of(ano, mes);
        var inicio = periodo.atDay(1); var fim = periodo.plusMonths(1).atDay(1);
        var lancs = producaoRepo.findNoPeriodo(inicio, fim);
        Map<String, BigDecimal> rec = new HashMap<>(), cst = new HashMap<>();

        for (var l: lancs){
            var nome = l.getProduto().getNome();
            rec.merge(nome, l.getProduto().getPrecoVenda().multiply(BigDecimal.valueOf(l.getQuantidade())), BigDecimal::add);
            var custoUnit = calcularCustoUnitario(l.getProduto().getIdProduto(), l.getData());
            cst.merge(nome, custoUnit.multiply(BigDecimal.valueOf(l.getQuantidade())), BigDecimal::add);
        }
        return rec.keySet().stream().sorted().map(n -> {
            var receita = rec.getOrDefault(n, BigDecimal.ZERO);
            var custo = cst.getOrDefault(n, BigDecimal.ZERO);
            var lucro = receita.subtract(custo);
            return new RelatorioMensalPorProdutoDTO(
                    n,
                    receita.setScale(2, RoundingMode.HALF_UP),
                    custo.setScale(2, RoundingMode.HALF_UP),
                    lucro.setScale(2, RoundingMode.HALF_UP)
            );
        }).collect(Collectors.toList());
    }

    private BigDecimal calcularCustoUnitario(UUID idProduto, LocalDate data){
        var custos = custoRepo.findVigentesNoDia(idProduto, data);
        if (custos.isEmpty()) return BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        for (var c: custos){
            var base = c.getCustoTotalCompra().divide(c.getQuantidadeTotalCompra(), 10, RoundingMode.HALF_UP);
            total = total.add(base.multiply(c.getConsumoPorUnidade()));
        }
        return total;
    }
}