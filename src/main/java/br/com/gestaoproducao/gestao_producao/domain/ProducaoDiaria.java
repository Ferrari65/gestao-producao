package br.com.gestaoproducao.gestao_producao.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "producao_diaria",
        uniqueConstraints = @UniqueConstraint(name = "unq_produto_data", columnNames = {"id_produto","data"}))
public class ProducaoDiaria {
    @Id
    @GeneratedValue
    @Column(name = "id_lancamento", columnDefinition = "uuid")
    private UUID idLancamento;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @Column(nullable = false) private LocalDate data;
    @Column(nullable = false) private Integer quantidade;
    @Column private String observacao;
}