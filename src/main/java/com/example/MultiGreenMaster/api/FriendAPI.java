package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.FriendSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friend")
public class FriendAPI {
    @Autowired
    private FriendSRV friendSRV;

    /* 친구 추가 api */
    // 0 : 정상작동, 1 : 이미 친구인 상태, 2 : 기타 오류
    @GetMapping("/add/{friendId}")
    public ResponseEntity<Map<String, Object>> addFriend(@PathVariable Long friendId, HttpSession session) {
        return ResponseEntity.ok(friendSRV.addFriend(friendId, session));
    }

    /* 친구 삭제 메소드 */
    // 0 : 정상작동, 1 : 이미 친구 아닌 상태, 2 : 기타 오류 발생
    @GetMapping("/remove/{friendId}")
    public ResponseEntity<Map<String, Object>> removeFriend(@PathVariable Long friendId, HttpSession session) {
        return ResponseEntity.ok(friendSRV.removeFriend(friendId, session));
    }

    @GetMapping("/getallfriend")
    public ResponseEntity<List<UserENT>> getAllFriendList(HttpSession session) {
        List<UserENT> targetList = friendSRV.findUsersFriends(session);
        if(targetList == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(targetList);
    }

    @GetMapping("/isfriendwith/{friendID}")
    public ResponseEntity<Boolean> isFriendWith(@PathVariable Long friendId, HttpSession session) {
        Boolean result = friendSRV.isFriend(session, friendId);
        if (result == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(result);
    }
}
