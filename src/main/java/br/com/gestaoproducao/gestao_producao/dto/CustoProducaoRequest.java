package br.com.gestaoproducao.gestao_producao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CustoProducaoRequest(UUID idProduto,
                                   String nomeInsumo,
                                   BigDecimal custoTotalCompra,
                                   BigDecimal quantidadeTotalCompra,
                                   String unidadeBase,
                                   BigDecimal consumoPorUnidade,
                                   LocalDate vigenteDe,
                                   LocalDate vigenteAte) {}