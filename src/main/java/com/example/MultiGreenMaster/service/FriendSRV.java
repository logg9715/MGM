package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.FriendENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.repository.FriendREP;
import com.example.MultiGreenMaster.repository.UserREP;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class FriendSRV {
    @Autowired
    private FriendREP friendREP;

    @Autowired
    private UserREP userREP;

    @Transactional
    /* 친구 추가 메소드 */
    // 0 : 정상작동, 1 : 이미 친구인 상태, 2 : 기타 오류 발생
    public Map<String, Object> addFriend(Long friendId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        // 1. 세션 로그인 정보가 없으면 튕기게 함.
        Object sessionUserId = session.getAttribute("userId");
        if(sessionUserId == null) {
            result.put("friendresult", 2);
            return result;
        }

        // 2. 세션의 고유코드로 계정을 검색했는데 없는 경우 튕기게 함.
        UserENT userENT_my = userREP.findById((Long)sessionUserId).orElse(null);
        if(userENT_my == null) {
            result.put("friendresult", 2);
            return result;
        }

        // 3. 자기 자신을 친구로 넣으려면 튕기게 함.
        if(((Long) sessionUserId).equals(friendId)){
            result.put("friendresult", 2);
            return result;
        }

        /* 상대방 계정 정보를 못 찾으면 2 반환 */
        UserENT friendENT = userREP.findById(friendId).orElse(null);
        if (friendENT == null) {
            result.put("friendresult", 2);
            return result;
        }

        /* 이미 친구인 경우 1 반환 */
        if (friendREP.existsByUserAndFriend(userENT_my, friendENT))  {
            result.put("friendresult", 1);
            return result;
        }

        // 친구 추가
        FriendENT newFriend = FriendENT.builder().id(null).user(userENT_my).friend(friendENT).build();
        FriendENT target = friendREP.save(newFriend);
        if (target == null)
            result.put("friendresult", 2);
        else
            result.put("friendresult", 0);
        return result;
    }

    @Transactional
    public Map<String, Object> removeFriend(Long friendId, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        // 1. 세션 로그인 정보가 없으면 튕기게 함.
        Object sessionUserId = session.getAttribute("userId");
        if(sessionUserId == null) {
            result.put("friendresult", 2);
            return result;
        }

        // 2. 세션의 고유코드로 계정을 검색했는데 없는 경우 튕기게 함.
        UserENT userENT_my = userREP.findById((Long)sessionUserId).orElse(null);
        if(userENT_my == null) {
            result.put("friendresult", 2);
            return result;
        }

        // 3. 자기 자신을 친구로 넣으려면 튕기게 함.
        if(((Long) sessionUserId).equals(friendId)){
            result.put("friendresult", 2);
            return result;
        }

        /* 상대방 계정 정보를 못 찾으면 2 반환 */
        UserENT friend = userREP.findById(friendId).orElse(null);
        if (friend == null) {
            result.put("friendresult", 2);
            return result;
        }

        /* 이미 친구가 아닌 경우 1 반환 */
        if(!friendREP.existsByUserAndFriend(userENT_my,friend)) {
            result.put("friendresult", 1);
            return result;
        }

        // 친구 삭제
        friendREP.delete(friendREP.findByUser(userENT_my));
        result.put("friendresult", 0);
        return result;
    }
}
