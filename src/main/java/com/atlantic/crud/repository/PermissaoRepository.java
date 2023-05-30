package com.atlantic.crud.repository;

import com.atlantic.crud.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    Permissao findByCodigo(String codigo);
}
