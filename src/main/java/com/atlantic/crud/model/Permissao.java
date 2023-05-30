package com.atlantic.crud.model;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Permissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private String descricao;

    public Permissao() {
    }

    public Permissao(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}