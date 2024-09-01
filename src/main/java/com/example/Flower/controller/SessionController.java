package com.example.Flower.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public class SessionController {//각 컨트롤러마다 로그인 세션 값을 상속해주기 위한 컨트롤러.
    @Autowired
    private HttpServletRequest request;

    @ModelAttribute("loggedInUserId")
    public String loggedInUserId() {
        HttpSession session = request.getSession(false);
        return (session != null && session.getAttribute("userId") != null) ? (String) session.getAttribute("userId") : null;
    }

    @ModelAttribute("loggedIn")
    public boolean loggedIn() {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }
}
