package br.com.gestaoproducao.gestao_producao.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue
    @Column (name = "id_produto", columnDefinition = "uuid")
    private UUID idProduto;

    private String nome;

    @Column (name = "unidade_venda")
    private String unidadeVenda;

    @Column(name = "preco_venda", precision = 10, scale = 2)
    private BigDecimal precoVenda;

}
