package br.com.gestaoproducao.gestao_producao.dto;

import java.math.BigDecimal;

public record RelatorioMensalConsolidadoDTO(
        int ano,
        int mes,
        BigDecimal receita,
        BigDecimal custo,
        BigDecimal lucro) {}