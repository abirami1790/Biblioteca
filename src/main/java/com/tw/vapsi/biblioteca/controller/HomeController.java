package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.UserService;
import com.tw.vapsi.biblioteca.service.dto.UserDetailsDTO;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    private String showWelcome(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String email = loggedInUser instanceof AnonymousAuthenticationToken ? "" : loggedInUser.getName();

        if (!email.isEmpty()) {
            User user = userService.findByEmail(email);
            model.addAttribute("userId", user.getId());
            model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());
        } else {
            model.addAttribute("userId", null);
        }

        return "index";
    }
}