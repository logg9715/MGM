package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/friend")
public class FriendAPI {
    @Autowired
    UserSRV userSRV;

    /* 친구 추가 api */
    // 0 : 정상작동, 1 : 이미 친구인 상태, 2 : 기타 오류
    @GetMapping("/add/{friendId}")
    public ResponseEntity<Map<String, Object>> addFriend(@PathVariable Long friendId, HttpSession session)
    {
        Map<String, Object> result = new HashMap<>();
        // 1. 세션 로그인 정보가 없으면 튕기게 함.
        Object sessionUserId = session.getAttribute("userId");
        if(sessionUserId == null) {
            result.put("friendresult", 2);
            return ResponseEntity.ok(result);
        }

        // 2. 세션의 고유코드로 계정을 검색했는데 없는 경우 튕기게 함.
        UserENT userENT_my = userSRV.findUserById((Long) sessionUserId);
        if(userENT_my == null) {
            result.put("friendresult", 2);
            return ResponseEntity.ok(result);
        }

        // 3. 자기 자신을 친구로 넣으려면 튕기게 함.
        if(((Long) sessionUserId).equals(friendId)){
            result.put("friendresult", 2);
            return ResponseEntity.ok(result);
        }

        // 정상 작동
        Integer userServiceResult = userSRV.addFriend(userENT_my.getId(), friendId);
        result.put("friendresult", userServiceResult);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/remove/{friendId}")
    public ResponseEntity<Map<String, Object>> removeFriend(@PathVariable Long friendId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        // 1. 세션 로그인 정보가 없으면 튕기게 함.
        Object sessionUserId = session.getAttribute("userId");
        if(sessionUserId == null) {
            result.put("friendresult", 2);
            return ResponseEntity.ok(result);
        }

        // 2. 세션의 고유코드로 계정을 검색했는데 없는 경우 튕기게 함.
        UserENT userENT_my = userSRV.findUserById((Long) sessionUserId);
        if(userENT_my == null) {
            result.put("friendresult", 2);
            return ResponseEntity.ok(result);
        }

        // 3. 자기 자신을 친구로 넣으려면 튕기게 함.
        if(((Long) sessionUserId).equals(friendId)){
            result.put("friendresult", 2);
            return ResponseEntity.ok(result);
        }

        // 정상 작동
        Integer userServiceResult = userSRV.removeFriend(userENT_my.getId(), friendId);
        result.put("friendresult", userServiceResult);
        return ResponseEntity.ok(result);
    }
}
