package com.example.MultiGreenMaster.controller;

import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.CMRecommentSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/guestbook")
public class CMGuestbookCTL {

    @Autowired
    private CMRecommentSRV cmRecommentService;
    @Autowired
    private UserSRV userService;

    @GetMapping("/{userId}")
    public String showGuestbook(@PathVariable Long userId, Model model) {
        UserENT user = userService.getLoginUserById(userId);
        if (user == null) {
            return "redirect:/session-login/login";
        }

        List<Object[]> commentsAndRecomments = cmRecommentService.findCommentsAndRecommentsByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("commentsAndRecomments", commentsAndRecomments);
        return "guestbookpage"; // Mustache 템플릿 파일명
    }
}
