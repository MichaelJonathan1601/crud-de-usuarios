package com.atlantic.crud.controller;

import com.atlantic.crud.form.LoginForm;
import com.atlantic.crud.form.UserForm;
import com.atlantic.crud.model.User;
import com.atlantic.crud.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView showLoginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("LoginForm", new LoginForm());
        modelAndView.setViewName("login/login");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid @ModelAttribute("LoginForm") LoginForm login, BindingResult bindingResult, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("login/login");
        } else {
            User user = userService.findByEmail(login.getEmail());
            if (user == null || !userService.isPasswordValid(login.getEmail(), login.getSenha())) {
                modelAndView.addObject("error", "Credenciais inválidas. Por favor, tente novamente.");
                modelAndView.setViewName("login/login");
            } else if (!user.isAtivo()) {
                modelAndView.addObject("error", "Usuário esta inativo");
                modelAndView.setViewName("login/login");
            }else {
                session.setAttribute("userId", user.getId());
                modelAndView.setViewName("redirect:/profile/" + user.getId());
            }
        }

        return modelAndView;
    }

    @GetMapping("/profile/{id}")
    public ModelAndView getUserProfile(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getUserById(id);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user/profile");

        return modelAndView;
    }

    @GetMapping("/profile/edit/{email}")
    public ModelAndView showEditPage(@PathVariable("email") String email) {
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.findByEmail(email);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user/edit");
        return modelAndView;
    }

    @PostMapping("/profile/update")
    public ModelAndView updateProfile(@Valid @ModelAttribute("user") User userForm, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findByEmail(userForm.getEmail());

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/edit");
            return modelAndView;
        }


        user.setNome(userForm.getNome());
        user.setAtivo(userForm.isAtivo());

        userService.saveUser(user);

        modelAndView.addObject("user", user);
        modelAndView.setViewName("redirect:/profile/" + user.getId());

        return modelAndView;
    }


    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView();
        List<User> user = userService.getAllUsers();
        modelAndView.addObject("users", user);
        modelAndView.setViewName("user/register");

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid @ModelAttribute("userForm") UserForm user, BindingResult bindingResult) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        if (userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.userForm", "Email já cadastrado");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/register");
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedSenha = passwordEncoder.encode(user.getSenha());
            user.setSenha(hashedSenha);

            userService.registerUser(user);

            modelAndView.setViewName("redirect:/login");
        }

        return modelAndView;
    }
}