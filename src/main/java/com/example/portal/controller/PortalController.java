package com.example.portal.controller;

import com.example.portal.entity.Ad;
import com.example.portal.entity.User;
import com.example.portal.repository.AdRepository;
import com.example.portal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
public class PortalController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

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
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an account registered with the same email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/home?success";
    }

    @GetMapping("/login")
    public String loginUser() { return "login"; }

    @GetMapping("/announcement")
    public String userLogged(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String name = userRepository.findByEmail(principal.getName()).getName();
        model.addAttribute("name", name);
        return "announcement";
    }

    @GetMapping("/announcement/add")
    public String addAd(Ad ad, Model model) {
        model.addAttribute("ad");
        return "addAd";
    }

    @PostMapping("/announcement/add")
    public String saveAd(Ad ad, Model model) {
        model.addAttribute("ad", ad);
        ad.setCompanyName(ad.getCompanyName());
        ad.setCity(ad.getCity());
        ad.setPhone(ad.getPhone());
        ad.setDescriptionOrLink(ad.getDescriptionOrLink());
        ad.setTypeJob(ad.getTypeJob());
        ad.setDate(ad.getDate());
        ad.setAdType(ad.getAdType());
        adRepository.save(ad);
        return "redirect:/announcement/add?success";
    }

    @GetMapping("/announcement/list")
    public String seeListAds(Model model) {
        model.addAttribute("ads", adRepository.findAll());
        return "list";
    }
}
