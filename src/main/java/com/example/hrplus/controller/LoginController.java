package com.example.hrplus.controller;

import com.example.hrplus.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        // Validate the username and password (e.g., against a database)
        if (userService.isValidUser(username, password)) {
            // If valid, create a session and store user information
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            // Redirect to the home page or any other page
            return "redirect:/home";
        } else {
            // If invalid, redirect back to the login page with an error message
            return "redirect:/login?error";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate the current session
        request.getSession().invalidate();
        // Redirect to the login page
        return "redirect:/login";
    }

}
