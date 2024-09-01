package com.example.Flower.dto;

import com.example.Flower.entity.User;
import com.example.Flower.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

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

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                //추가
                .name(this.name)
                .age(this.age)
                .phonenumber(this.phonenumber)
                .email(this.email)
                //추가
                .role(UserRole.USER)
                .active(1)
                .build();
    }

}