package br.com.gestaoproducao.gestao_producao.dto;

import java.math.BigDecimal;

public record RelatorioMensalPorProdutoDTO(
        String nomeProduto,
        BigDecimal receita,
        BigDecimal custo,
        BigDecimal lucro) {}

