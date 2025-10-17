package br.com.gestaoproducao.gestao_producao.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ProducaoDiariaRequest(
        UUID idProduto,
        LocalDate data,
        Integer quantidade,
        String observacao) { }
