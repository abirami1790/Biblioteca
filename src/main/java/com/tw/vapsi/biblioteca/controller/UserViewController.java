package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.exception.UserException;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class UserViewController {
    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public String createUser(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String email,
                             @RequestParam String password, Model model) {
        try{
            User user = userService.save(firstName, lastName, email, password);
        }
        catch (UserException exception){
            model.addAttribute("failure", exception.getMessage());
            return "signup-user";
        }

        return "login";
    }

    @GetMapping(path = "/signUp")
    public String showSignUpForm() {
        return "signup-user";
    }
}
