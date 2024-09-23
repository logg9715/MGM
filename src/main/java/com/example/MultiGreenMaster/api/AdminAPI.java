package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.Util.AccessAuthority;
import com.example.MultiGreenMaster.dto.UserFRM;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* 관리자 콘솔 페이지 */
// 모든 컨트롤러는 관리자가 아니면 badRequest로 응답한다.
@RestController
@RequestMapping("/api/admin")
public class AdminAPI {
    @Autowired
    UserSRV userSRV;

    /* 모든 유저 정보를 가져온다 (비활성화 된 유저 포함) */
    @GetMapping("/getuserlist")
    public ResponseEntity<List<UserENT>> getUserList(HttpSession httpSession) {
        AccessAuthority accessAuthority = new AccessAuthority(httpSession, userSRV);
        if (accessAuthority.forAdmin().isOk())
            return ResponseEntity.ok(userSRV.findAllUsers());
        else
            return ResponseEntity.badRequest().build();
    }

    /* 사용자 정보 업데이트 */
    // form에 유저 고유 코드가 필수로 있어야한다.
    // talend로만 테스트한다, 머스태치 테스트 미구현
    @PostMapping("/update")
    public ResponseEntity<UserENT> updateUser(@RequestBody UserFRM form, HttpSession session) {
        // 관리자 & 계정 본인의 경우만 허용, 아니면 badRequest 반환
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userSRV);
        if (accessAuthority.forAdmin().forOwner(form.getId()).isOk())
        {
            UserENT updatedUser = userSRV.updateUser(form);
            return (updatedUser == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(updatedUser);
        }
        else
            return ResponseEntity.badRequest().build();
    }

    /* 유저 비활성화 */
    @PostMapping("/{id}/disable")
    public ResponseEntity<UserENT> disableUser(@PathVariable Long id, HttpSession session) {
        // 관리자 계정이 아닌 경우 badRequest로 반환
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userSRV);
        if(accessAuthority.forAdmin().isOk())
        {
            UserENT target = userSRV.deactivateUser(id);
            if (target != null) // 정상적으로 계정이 비활성화 된 경우만 Entity를 반환
                return ResponseEntity.ok(target);
        }
        return ResponseEntity.badRequest().build();
    }

    /* 유저 활성화 */
    @PostMapping("/{id}/activate")
    public ResponseEntity<UserENT> activateUser(@PathVariable Long id, HttpSession session) {
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userSRV);
        if (accessAuthority.forAdmin().isOk())
        {
            UserENT target = userSRV.activateUser(id);
            if (target != null) // 정상적으로 반영 된 경우만 Entity를 반환
                return ResponseEntity.ok(target);
        }
        return ResponseEntity.badRequest().build();
    }

}
