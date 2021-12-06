package com.ecomerce.udemy.ecomerce.security;

import javax.validation.Valid;

import com.ecomerce.udemy.ecomerce.models.data.users.User;
import com.ecomerce.udemy.ecomerce.models.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String register(User user) {
        return "register";
    }

    @PostMapping
    public String register(@Valid User user, BindingResult bindingResult, Model model
    // ,@RequestParam(value = "client") String client
    ) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordMatchProblem", "Passwords do not match!");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/login";
    }

    // @PostMapping
    // public String newCompany(@Valid User user, BindingResult bindingResult, Model
    // model,
    // @RequestParam(value = "client") String client) {

    // if (bindingResult.hasErrors()) {
    // return "register";
    // }

    // if (!user.getPassword().equals(user.getConfirmPassword())) {
    // model.addAttribute("passwordMatchProblem", "Passwords do not match!");
    // return "register";
    // }

    // user.setPassword(passwordEncoder.encode(user.getPassword()));
    // userRepository.save(user);

    // return "redirect:/login";
    // }
}
