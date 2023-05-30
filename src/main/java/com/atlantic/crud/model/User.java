package com.atlantic.crud.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private User responsavel;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Permissao> permissoes;


    public User() {
    }

    public User(String nome, String email, String senha, String cpf, boolean ativo, User responsavel) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.ativo = ativo;
        this.responsavel = responsavel;
        this.permissoes = new ArrayList<>();
    }
}