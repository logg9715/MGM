package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.Util.AccessAuthority;
import com.example.MultiGreenMaster.dto.UserFRM;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/usermanage") // 기본 경로 설정
public class UserManageAPI {

    @Autowired
    private UserSRV userService; // UserService 의존성 주입

    /* 사용자 추가 */
    @PostMapping("/create")
    public ResponseEntity<UserENT> createUser(@RequestBody UserFRM form) {
        // 새로운 사용자를 생성
        log.info(form.toString());
        UserENT user = form.toEntity();
        log.info(user.toString());

        // 중복체크
        if (form.getId() != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (userService.isLoginIdDuplicate(user.getLoginId())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if (userService.isNicknameDuplicate(user.getNickname())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.ok(userService.saveUser(user));
    }

    //사용자 id(loginId) 중복체크
    @GetMapping("/check-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateLoginId(@RequestParam String loginId) {
        boolean isDuplicate = userService.isLoginIdDuplicate(loginId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }
    /* 사용자 닉네임 중복체크 */
    @GetMapping("/check-nickname-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateNickname(@RequestParam String nickname) {
        boolean isDuplicate = userService.isNicknameDuplicate(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    /* 테스트용 메소드 지울예정 */
    @GetMapping("/tmp/{id}")
    public ResponseEntity<UserENT> tmp(@PathVariable Long id) {
        UserENT target = userService.getLoginUserById(id);
        return ResponseEntity.ok(target);
    }
}
