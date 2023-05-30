package com.atlantic.crud.form;

import com.atlantic.crud.model.Permissao;
import com.atlantic.crud.model.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@EqualsAndHashCode
public class ProfileUpdateForm {

    private String nome;
    private String email;
    private boolean ativo;
    private User responsavel;
    private List<Permissao> permissoes;
}
