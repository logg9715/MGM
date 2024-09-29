package com.example.MultiGreenMaster.Util;

import com.example.MultiGreenMaster.entity.FriendENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.entity.User_RoleENUM;
import com.example.MultiGreenMaster.service.FriendSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;

@Getter
public class AccessAuthority {
    private UserSRV userSRV;
    private UserENT userENT;
    private FriendSRV friendSRV;
    private boolean ok;

    /* 생성자는 (session=api세션, UserSRV=컨트롤러에서 주입된 UserSRV) 를 필수로 가진다. */
    public AccessAuthority(HttpSession session, UserSRV userSRV) {
        this.userSRV = userSRV;
        this.ok = false;

        if (session.getAttribute("userId") == null)
            this.userENT = null;
        else
            this.userENT = this.userSRV.findUserById((Long)session.getAttribute("userId"));
    }

    /* 친구 기능 활용하는 생성자 */
    public AccessAuthority(HttpSession session, UserSRV userSRV, FriendSRV friendSRV) {
        this.userSRV = userSRV;
        this.friendSRV = friendSRV;
        this.ok = false;

        if (session.getAttribute("userId") == null)
            this.userENT = null;
        else
            this.userENT = this.userSRV.findUserById((Long)session.getAttribute("userId"));
    }


    public AccessAuthority forAdmin() {
        if (this.userENT == null) return this;

        if (this.userENT.getRole() == User_RoleENUM.ADMIN)
            this.ok = true;
        return this;
    }

    public AccessAuthority forOwner(Long targetOwnerId) {
        if (this.userENT == null) return this;

        if (this.userENT.getId().equals(targetOwnerId))
            this.ok = true;
        return this;
    }

    /* 이 경우, 생성자의 session=접속시도자, friendId가 글 주인 id가 된다 */
    // 둘의 친구 관계만 확인하면 되서 상관 x
    public AccessAuthority forFriend(Long friendId) {
        if (this.userENT == null) return this;

        if (friendSRV.isFriendforAccess(this.userENT.getId(), friendId))
            this.ok = true;
        return this;
    }
}
