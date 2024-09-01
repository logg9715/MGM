package com.example.Flower.controller;


import com.example.Flower.service.UsersService;

import com.example.Flower.entity.Users;
import com.example.Flower.repository.UsersRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController extends SessionController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        if (usersService.validateUser(userId, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", userId);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid userId or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }
}

