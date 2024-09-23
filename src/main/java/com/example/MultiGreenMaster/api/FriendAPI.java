package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.entity.FriendENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/friend")
public class FriendAPI {
    UserSRV userSRV;

    /* 친구 추가 api */
    // 0 : 정상작동, 1 : 이미 친구인 상태, 2 : 기타 오류
    @PostMapping("/addfriend/{friendId}")
    public ResponseEntity<Map<String, Object>> addFriend(@PathVariable Long friendId, HttpSession session)
    {
        Map<String, Object> result = new HashMap<>();
        // 세션 로그인 정보가 없으면 튕기게 함.
        Object sessionUserId = session.getAttribute("userId");
        if(sessionUserId == null) {
            result.put("friendresult", 2);
            return ResponseEntity.ok(result);
        }

        UserENT userENT_my = userSRV.findUserById((Long) sessionUserId);
        if(userENT_my == null) {
            result.put("friendresult", 2);
            return ResponseEntity.ok(result);
        }

        Integer userServiceResult = userSRV.addFriend(userENT_my.getId(), friendId); // 친구 추가 메서드 호출
        result.put("friendresult", userServiceResult);

        return ResponseEntity.ok(result);
    }
/*
    @PostMapping("/{id}/removefriend")
    public String removeFriend(@PathVariable Long id, @RequestParam Long friendId, RedirectAttributes redirectAttributes) {
        try {
            userSRV.removeFriend(id, friendId); // 친구 삭제 메서드 호출
            redirectAttributes.addFlashAttribute("msg", "친구가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "친구를 삭제하는 중 오류가 발생했습니다.");
        }
        return "redirect:/user/" + id; // 유저 페이지로 리다이렉트
    }
 */
}
