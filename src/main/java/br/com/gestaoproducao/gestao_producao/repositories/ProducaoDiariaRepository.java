package br.com.gestaoproducao.gestao_producao.repositories;

import br.com.gestaoproducao.gestao_producao.domain.ProducaoDiaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProducaoDiariaRepository extends JpaRepository<ProducaoDiaria, UUID> {
    @Query("SELECT p FROM ProducaoDiaria p WHERE p.data >= :inicio AND p.data < :fim")
    List<ProducaoDiaria> findNoPeriodo(LocalDate inicio, LocalDate fim);

    @Query("SELECT p FROM ProducaoDiaria p WHERE p.produto.idProduto = :id AND p.data >= :inicio AND p.data < :fim")
    List<ProducaoDiaria> findDoProdutoNoPeriodo(UUID id, LocalDate inicio, LocalDate fim);
}