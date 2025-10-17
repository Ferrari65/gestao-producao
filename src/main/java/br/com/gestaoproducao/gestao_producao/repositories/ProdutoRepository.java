package br.com.gestaoproducao.gestao_producao.repositories;

import br.com.gestaoproducao.gestao_producao.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository <Produto, UUID> {
    boolean existsByNomeIgnoreCase (String nome);

}
