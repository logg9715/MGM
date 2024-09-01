package com.example.Flower.api;

import com.example.Flower.dto.LoginRequest;
import com.example.Flower.entity.User;
import com.example.Flower.entity.UserRole;
import com.example.Flower.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session-login")
public class SessionLoginApiController {

    @Autowired
    private UserService userService;

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        User user = userService.login(loginRequest);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패했습니다. 잘못된 ID 또는 비밀번호입니다.");
        }

        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(1800);

        return ResponseEntity.ok("로그인 성공");
    }

    // 사용자 정보 조회
    @GetMapping("/info")
    public ResponseEntity<User> userInfo(@SessionAttribute(name = "userId", required = false) Long userId) {
        User loginUser = userService.getLoginUserById(userId);
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(loginUser);
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.noContent().build();
    }
}