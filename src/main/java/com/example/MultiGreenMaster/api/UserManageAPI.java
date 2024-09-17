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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /* 사용자 정보 업데이트 */
    // talend로만 테스트한다, 머스태치 테스트 미구현
    @PostMapping("/update")
    public ResponseEntity<UserENT> updateUser(@RequestBody UserFRM form, HttpSession session) {
        // 관리자 & 계정 본인의 경우만 허용, 아니면 badRequest 반환
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userService);
        if (accessAuthority.forAdmin().forOwner(form.getId()).isOk())
        {
            UserENT updatedUser = userService.updateUser(form);
            return (updatedUser == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(updatedUser);
        }
        else
            return ResponseEntity.badRequest().build();
    }

    /* 유저 비활성화 */
    @PostMapping("/{id}/disable")
    public ResponseEntity<UserENT> disableUser(@PathVariable Long id, HttpSession session) {
        // 관리자 계정이 아닌 경우 badRequest로 반환
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userService);
        if(accessAuthority.forAdmin().isOk())
        {
            UserENT target = userService.deactivateUser(id);
            if (target != null) // 정상적으로 계정이 비활성화 된 경우만 Entity를 반환
                return ResponseEntity.ok(target);
        }
        return ResponseEntity.badRequest().build();
    }

    /* 유저 활성화 */
    @PostMapping("/{id}/activate")
    public ResponseEntity<UserENT> activateUser(@PathVariable Long id, HttpSession session) {
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userService);
        if (accessAuthority.forAdmin().isOk())
        {
            UserENT target = userService.activateUser(id);
            if (target != null) // 정상적으로 반영 된 경우만 Entity를 반환
                return ResponseEntity.ok(target);
        }
        return ResponseEntity.badRequest().build();
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

    /* 비활성화 유저 목록 불러오기 */
    @GetMapping("/inactive-list")
    public ResponseEntity<List<UserENT>> inactiveUserIndex() {
        List<UserENT> inactiveUserList = userService.findInactiveUsers(); // 비활성화된 사용자 목록을 모델에 추가
        return ResponseEntity.ok(inactiveUserList);
    }

    /* 테스트용 메소드 지울예정 */
    @GetMapping("/tmp/{id}")
    public ResponseEntity<UserENT> tmp(@PathVariable Long id) {
        UserENT target = userService.getLoginUserById(id);
        return ResponseEntity.ok(target);
    }
}
