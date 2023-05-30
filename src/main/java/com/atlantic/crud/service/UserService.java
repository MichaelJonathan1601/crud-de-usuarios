package com.atlantic.crud.service;

import com.atlantic.crud.form.UserForm;
import com.atlantic.crud.model.Permissao;
import com.atlantic.crud.model.User;
import com.atlantic.crud.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserForm userForm) throws Exception {

        User user = new User(userForm.getNome(), userForm.getEmail(), userForm.getSenha(), userForm.getCpf(),userForm.isAtivo(), userForm.getResponsavel());

        if (!isEmailValid(user.getEmail())) {
            throw new Exception("E-mail inválido");
        }

        if (!isCPFValid(user.getCpf())) {
            throw new Exception("CPF inválido");
        }


        // Definir permissão "Acesso ao sistema" para o novo usuário
        Permissao acessoSistema = new Permissao("ACESSO_SISTEMA", "Acesso ao sistema");
        user.getPermissoes().add(acessoSistema);

        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new IllegalArgumentException("Usuário não encontrado com o ID: " + userId);
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(email));
        return existingUser.isPresent();
    }

    public boolean isPasswordValid(String email, String senha) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedSenha = user.getSenha();
            return passwordEncoder.matches(senha, hashedSenha);
        }
        return false;
    }


    private boolean isEmailValid(String email) {
        EmailValidator validator = EmailValidator.getInstance();

        return validator.isValid(email);
    }

    private boolean isCPFValid(String cpf) {

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos do CPF são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador do CPF
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * (10 - i);
        }
        int digit1 = 11 - (sum % 11);
        if (digit1 == 10 || digit1 == 11) {
            digit1 = 0;
        }

        // Calcula o segundo dígito verificador do CPF
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * (11 - i);
        }
        int digit2 = 11 - (sum % 11);
        if (digit2 == 10 || digit2 == 11) {
            digit2 = 0;
        }

        // Verifica se os dígitos verificadores do CPF são válidos
        return digit1 == (cpf.charAt(9) - '0') && digit2 == (cpf.charAt(10) - '0');
    }
}
