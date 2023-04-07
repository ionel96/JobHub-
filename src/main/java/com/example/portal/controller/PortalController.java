package com.example.portal.controller;

import com.example.portal.entity.User;
import com.example.portal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PortalController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String portalAnnouncements() {
        return "home";
    }

    @GetMapping("/register")
    public String registerUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/add")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an account registered with the same email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/home?success";
    }

    @GetMapping("/login")
    public String loginUser() { return "login"; }

    @GetMapping("/logged")
    public String userLogged() { return "logged"; }
}
