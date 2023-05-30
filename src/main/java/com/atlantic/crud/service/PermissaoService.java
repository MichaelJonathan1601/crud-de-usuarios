package com.atlantic.crud.service;

import com.atlantic.crud.model.Permissao;
import com.atlantic.crud.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    @Autowired
    public PermissaoService(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    public List<Permissao> findAll(){
       return permissaoRepository.findAll();
    }

    public Permissao findByCodigo(String codigo){
        return permissaoRepository.findByCodigo(codigo);
    }

}
