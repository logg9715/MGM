package com.example.MultiGreenMaster.Util;

import com.example.MultiGreenMaster.entity.FriendENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.entity.User_RoleENUM;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;

@Getter
public class AccessAuthority {
    private UserSRV userSRV;
    private UserENT userENT;
    private FriendENT friendENT;
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

    public AccessAuthority forAdmin() {
        if (this.userENT == null) return this;

        System.out.println("1-2 : " + this.userENT.getRole());
        if (this.userENT.getRole() == User_RoleENUM.ADMIN)
            this.ok = true;
        return this;
    }

    public AccessAuthority forOwner(Long targetOwnerId) {
        if (this.userENT == null) return this;

        System.out.println("1-3");
        if (this.userENT.getId().equals(targetOwnerId))
            this.ok = true;
        return this;
    }
}
