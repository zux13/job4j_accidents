package ru.job4j.accidents.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.SpringDataAuthorityRepository;
import ru.job4j.accidents.repository.SpringDataUserRepository;

@Controller
@AllArgsConstructor
public class RegController {

    private final PasswordEncoder encoder;
    private final SpringDataUserRepository users;
    private final SpringDataAuthorityRepository authorities;

    @PostMapping("/register")
    public String regSave(@ModelAttribute User user) {
        user.setEnabled(true);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthority(authorities.findByAuthority("ROLE_USER"));
        users.save(user);
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String regPage(Model model) {
        model.addAttribute("user", new User());
        return "/users/register";
    }
}
