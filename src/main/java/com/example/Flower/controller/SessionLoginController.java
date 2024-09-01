package com.example.Flower.controller;

import com.example.Flower.dto.LoginRequest;
import com.example.Flower.entity.User;
import com.example.Flower.entity.UserRole;
import com.example.Flower.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/session-login")
public class SessionLoginController extends SessionCheckController {

    @Autowired
    private UserService userService;

    // 홈 페이지로 이동
    @GetMapping(value = {"", "/"})
    public String home(Model model) {
        return "sessionLogin/home";
    }

    // 회원가입 페이지로 이동
    @GetMapping("/join")
    public String joinPage(Model model) {
        return "sessionLogin/join";
    }

    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "sessionLogin/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest,
                        HttpServletRequest httpServletRequest, Model model, RedirectAttributes rttr) {
        User user = userService.login(loginRequest);

        if (user == null) {
            rttr.addFlashAttribute("error", "로그인에 실패했습니다. 잘못된 ID 또는 비밀번호입니다.");
            return "redirect:/session-login/login";
        }

        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(1800);
        return "redirect:/session-login";
    }

    // 사용자 정보 페이지로 이동
    @GetMapping("/info")
    public String userInfo(@SessionAttribute(name="userId", required = false) Long userId, Model model) {
        User loginUser = userService.getLoginUserById(userId);
        if (loginUser == null) {
            return "redirect:/session-login/login";
        }
        model.addAttribute("user", loginUser);
        return "sessionLogin/info";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    // 마이 페이지로 이동
    @GetMapping("/mypage")
    public String myPage(Model model) {
        return "sessionLogin/mypage";
    }

    // 관리자 페이지로 이동
    @GetMapping("/admin")
    public String adminPage(@SessionAttribute(name="userId", required = false) Long userId, Model model) {
        User loginUser = userService.getLoginUserById(userId);
        if (loginUser == null) {
            return "redirect:/session-login/login";
        }
        if (!loginUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/session-login/mypage";
        }
        return "sessionLogin/admin";
    }
}