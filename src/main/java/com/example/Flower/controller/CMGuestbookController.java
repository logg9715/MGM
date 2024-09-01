package com.example.Flower.controller;

import com.example.Flower.entity.User;
import com.example.Flower.service.CMRecommentService;
import com.example.Flower.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/guestbook")
public class CMGuestbookController {

    @Autowired
    private CMRecommentService cmRecommentService;
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public String showGuestbook(@PathVariable Long userId, Model model) {
        User user = userService.getLoginUserById(userId);
        if (user == null) {
            return "redirect:/session-login/login";
        }

        List<Object[]> commentsAndRecomments = cmRecommentService.findCommentsAndRecommentsByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("commentsAndRecomments", commentsAndRecomments);
        return "guestbookpage"; // Mustache 템플릿 파일명
    }
}
