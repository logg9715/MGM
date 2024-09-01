package com.example.Flower.controller;

import com.example.Flower.entity.UserRole;
import com.example.Flower.entity.User;
import com.example.Flower.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

public class SessionCheckController {
    //로그인 한 아이디 체크를 위한 코드
    @Autowired // UserService Bean 객체를 주입
    private UserService userService;
    @ModelAttribute//공통 데이터를 모델에 추가할 때 @ModelAttribute를 사용하여 컨트롤러 레벨에서 공통 처리를 할 수 있습니다.
    public void addAttributes(Model model,
                              @SessionAttribute(name="userId", required=false) Long userId) {
        if (userId != null) {
            User loginUser = userService.getLoginUserById(userId); // userId로 로그인한 사용자 정보 조회
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname()); // 사용자 닉네임을 모델에 추가
                model.addAttribute("isOwner", loginUser.getId()); // 사용자 id를 모델에 추가
            }
        }
    }
    //로그인 한 아이디가 관리자 여부인가를 체크
    @ModelAttribute
    public void adminUserCheck(Model model, @SessionAttribute(name="userId", required=false) Long userId) {
        if (userId != null) {
            User loginUser = userService.getLoginUserById(userId);
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
                if (loginUser.getRole().equals(UserRole.ADMIN)) {
                    model.addAttribute("isAdmin", true);
                } else {
                    model.addAttribute("isAdmin", false);
                }
            }
        }
    }
}
