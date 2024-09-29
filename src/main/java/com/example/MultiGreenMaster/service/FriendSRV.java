package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.dto.UserDTO_id_nickname;
import com.example.MultiGreenMaster.entity.FriendENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.repository.FriendREP;
import com.example.MultiGreenMaster.repository.UserREP;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
        FriendENT friendship = friendREP.findByUserAndFriend(userENT_my, friend);
        if (friendship != null) {
            friendREP.delete(friendship);
            result.put("friendresult", 0); // 성공적으로 삭제된 경우
        } else {
            result.put("friendresult", 2); // 기타 오류
        }

        return result;
    }

    //사용자와 친구 데이터를 미리 로드하여 컨트롤러로 전달
    @Transactional
    public List<UserDTO_id_nickname> findUsersFriends(HttpSession session) {
        System.out.println("@@@@ : 1");
        Object userId = session.getAttribute("userId");
        if(userId == null)
            return null;

        System.out.println("@@@@ : 2");
        return friendREP.findAllFriendByUserId((Long) userId);
    }

    public Boolean isFriend(HttpSession session, Long friendId) {
        /* 세션 로그인 오류 */
        Object userId = session.getAttribute("userId");
        if(userId == null)
            return null;

        /* 계정 정보 찾기 오류 */
        UserENT userENT_my = userREP.findById((Long) userId).orElse(null);
        UserENT friendENT = userREP.findById(friendId).orElse(null);
        if(userENT_my == null || friendENT == null)
            return null;

        return friendREP.existsByUserAndFriend(userENT_my, friendENT);
    }

    /* 접근제한 클래스에서 쓰는 전용 메소드 */
    public Boolean isFriendforAccess(Long userId, Long friendId) {
        /* 계정 정보 찾기 오류 */
        UserENT userENT_my = userREP.findById(userId).orElse(null);
        UserENT friendENT = userREP.findById(friendId).orElse(null);
        if(userENT_my == null || friendENT == null)
            return null;

        return friendREP.existsByUserAndFriend(userENT_my, friendENT);
    }
}
