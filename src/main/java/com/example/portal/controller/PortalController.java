package com.example.portal.controller;

import com.example.portal.entity.Role;
import com.example.portal.repository.RoleRepository;
import com.example.portal.entity.User;
import com.example.portal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PortalController {

    @Autowired
    private UserRepository userRepository;

    private RoleRepository roleRepository;

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
        System.out.println(user.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role role = new Role();
        role.setName("USER");
        //roleRepository.save(role);
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an account registered with the same email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        //userRepository.save(user);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        return "redirect:/home?success";
    }

    @GetMapping("/login")
    public String loginUser() {
        return "login";
    }
}


