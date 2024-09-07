package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.entity.User_RoleENT;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequestFRM {

    private String loginId;
    private String password;
    private String passwordCheck;
    private String nickname;

    //추가
    private String name;
    private int age;
    private String phonenumber;
    private String email;
    //추가

    public UserENT toEntity() {
        return UserENT.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                //추가
                .name(this.name)
                .phonenumber(this.phonenumber)
                .email(this.email)
                //추가
                .role(User_RoleENT.USER)
                .active(1)
                .build();
    }

}