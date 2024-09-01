package com.example.Flower.dto;

import com.example.Flower.entity.UserRole;
import com.example.Flower.entity.User;
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
    private int age;
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
                .age(this.age)
                .phonenumber(this.phonenumber)
                .email(this.email)
                .role(this.role != null ? this.role : UserRole.USER) // 기본값 설정
                .active(1)
                .build();
    }
}