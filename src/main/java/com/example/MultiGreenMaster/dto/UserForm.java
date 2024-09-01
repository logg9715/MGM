package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.UserRole;
import com.example.MultiGreenMaster.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserForm {

    private Long id;
    private String loginId;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String name;
    private String phonenumber;
    private String email;
    private UserRole role; // role 필드 추가
    private int active;

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .name(this.name)
                .phonenumber(this.phonenumber)
                .email(this.email)
                .role(this.role != null ? this.role : UserRole.USER) // 기본값 설정
                .active(1)
                .build();
    }
}