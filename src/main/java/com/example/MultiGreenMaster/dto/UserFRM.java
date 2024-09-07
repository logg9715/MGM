package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.User_RoleENT;
import com.example.MultiGreenMaster.entity.UserENT;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserFRM {

    private Long id;
    private String loginId;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String name;
    private String phonenumber;
    private String email;
    private User_RoleENT role; // role 필드 추가
    private int active;

    public UserENT toEntity() {
        return UserENT.builder()
                .id(this.id)
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .name(this.name)
                .phonenumber(this.phonenumber)
                .email(this.email)
                .role(this.role != null ? this.role : User_RoleENT.USER) // 기본값 설정
                .active(1)
                .build();
    }
}