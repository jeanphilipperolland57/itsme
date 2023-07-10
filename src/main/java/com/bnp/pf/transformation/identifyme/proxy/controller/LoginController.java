package com.bnp.pf.transformation.identifyme.proxy.controller;

import com.bnp.pf.transformation.identifyme.proxy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/error")
    public String error() {
        return "error.html";
    }

    @GetMapping("/login/itsme")
    public String redirectToItsme() {
        return "redirect:/oauth2/authorization/itsme";
    }

    @GetMapping("/result")
    public String result(Model model, @AuthenticationPrincipal OAuth2User user) {
        var result = userService.findById(user.getName());

        model.addAttribute("email", result.getEmail());
        model.addAttribute("firstName", result.getFirstName());
        model.addAttribute("lastName", result.getLastName());
        model.addAttribute("dob", result.getDateOfBirth());

        return "result.html";
    }

    @GetMapping("/login/oauth2/code/itsme")
    public String itsmeLoginCallback(Model model, @AuthenticationPrincipal OAuth2User user) {
        userService.saveUserData(user);
        log.info("User saved successfully {}", user.getName());

        return "redirect:/result";
    }
}
