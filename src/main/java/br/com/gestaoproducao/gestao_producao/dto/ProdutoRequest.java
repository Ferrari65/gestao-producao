package br.com.gestaoproducao.gestao_producao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
public record ProdutoRequest(
        @Schema(example = "Cone Trufado")
        @NotBlank String nome,

        @Schema(example = "unidade")
        @NotBlank String unidadeVenda,

        @Schema(example = "5.50", minimum = "0.0")
        @NotNull @DecimalMin("0.0") java.math.BigDecimal precoVenda
) {}