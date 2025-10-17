package br.com.gestaoproducao.gestao_producao.repositories;

import br.com.gestaoproducao.gestao_producao.domain.CustoProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CustoProducaoRepository extends JpaRepository <CustoProducao, UUID> {

    @Query("""
      SELECT c FROM CustoProducao c
      WHERE c.produto.idProduto = :idProduto
        AND (:data BETWEEN COALESCE(c.vigenteDe, :data) AND COALESCE(c.vigenteAte, :data))
    """)
    List<CustoProducao> findVigentesNoDia(UUID idProduto, LocalDate data);
}
