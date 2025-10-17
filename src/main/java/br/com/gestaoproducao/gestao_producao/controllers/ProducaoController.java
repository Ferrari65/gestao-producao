package br.com.gestaoproducao.gestao_producao.controllers;

import br.com.gestaoproducao.gestao_producao.domain.CustoProducao;
import br.com.gestaoproducao.gestao_producao.domain.ProducaoDiaria;
import br.com.gestaoproducao.gestao_producao.domain.Produto;
import br.com.gestaoproducao.gestao_producao.dto.*;
import br.com.gestaoproducao.gestao_producao.service.ProducaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Produção", description = "Cadastro de produtos, custos e lançamentos diários; relatórios mensais.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class ProducaoController {

    private final ProducaoService service;

    @Operation(summary = "Criar produto",
            description = "Registra um novo produto com preço de venda.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @PostMapping("/produtos")
    public ResponseEntity<Produto> criarProduto( @jakarta.validation.Valid @RequestBody ProdutoRequest req) {
        var p = service.criarProduto(req);
        return ResponseEntity.created(URI.create("/api/produtos/" + p.getIdProduto())).body(p);
    }

    @Operation(summary = "Adicionar custo (insumo) a um produto",
            description = "Registra insumo com custo/quantidade e consumo por unidade.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustoProducao.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @PostMapping("/produtos/{id}/custos")
    public ResponseEntity<CustoProducao> adicionarCusto(
            @PathVariable String id, @Valid @RequestBody CustoProducaoRequest req) {
        var c = service.adicionarCusto(req);
        return ResponseEntity.created(URI.create("/api/custos/" + c.getIdCusto())).body(c);
    }

    @Operation(summary = "Lançar produção do dia",
            description = "Cria um lançamento diário de quantidade produzida/vendida.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProducaoDiaria.class))),
            @ApiResponse(responseCode = "400", description = "Já existe lançamento para a data",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @PostMapping("/producao")
    public ResponseEntity<ProducaoDiaria> lancar(@Valid @RequestBody ProducaoDiariaRequest req) {
        var l = service.lancarProducao(req);
        return ResponseEntity.created(URI.create("/api/producao/" + l.getIdLancamento())).body(l);
    }

    @Operation(summary = "Relatório mensal (consolidado)",
            description = "Soma receita, custo e lucro de todos os produtos no mês.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RelatorioMensalConsolidadoDTO.class)))
    })
    @GetMapping("/relatorios/mensal")
    public ResponseEntity<RelatorioMensalConsolidadoDTO> consolidado(
            @Parameter(example = "2025") @RequestParam int ano,
            @Parameter(example = "10")   @RequestParam int mes) {
        return ResponseEntity.ok(service.relatorioConsolidado(ano, mes));
    }

    @Operation(summary = "Relatório mensal por produto",
            description = "Receita, custo e lucro por produto no mês.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RelatorioMensalPorProdutoDTO.class))))
    })
    @GetMapping("/relatorios/mensal/por-produto")
    public ResponseEntity<List<RelatorioMensalPorProdutoDTO>> porProduto(
            @Parameter(example = "2025") @RequestParam int ano,
            @Parameter(example = "10")   @RequestParam int mes) {
        return ResponseEntity.ok(service.relatorioPorProduto(ano, mes));
    }

    @Operation(summary = "Exportar Excel (XLSX)",
            description = "Baixa o relatório mensal por produto em XLSX.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Arquivo gerado",
                    content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            schema = @Schema(type = "string", format = "binary")))
    })
    @GetMapping(value = "/relatorios/mensal_por_produto.xlsx",
            produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public void exportExcel(/* ... */) {}
}