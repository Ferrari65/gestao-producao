package br.com.gestaoproducao.gestao_producao.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@Table(name = "custos_producao")
public class CustoProducao {

    @Id
    @GeneratedValue
    @Column (name = "id_custo", columnDefinition = "uuid")
    private UUID idCusto;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @Column (name = "nome_insumo")
    private String nomeInsumo;

    @Column(name = "custo_total_compra", precision = 12, scale = 2)
    private BigDecimal custoTotalCompra;

    @Column(name = "quantidade_total_compra", precision = 14, scale = 6)
    private BigDecimal quantidadeTotalCompra;

    @Column(name = "unidade_base")
    private String unidadeBase;

    @Column(name = "consumo_por_unidade", precision = 14, scale = 6)
    private BigDecimal consumoPorUnidade;

    @Column(name = "vigente_de") private LocalDate vigenteDe;
    @Column(name = "vigente_ate") private LocalDate vigenteAte;
}
