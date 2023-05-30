package com.atlantic.crud.form;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class LoginForm {
    private String email;
    private String senha;
}
