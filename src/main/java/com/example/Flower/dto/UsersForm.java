package com.example.Flower.dto;

import com.example.Flower.entity.Users;
import lombok.*;

@Getter
@Setter // 추가: Lombok이 set 메서드를 생성하도록 합니다.
@AllArgsConstructor
@NoArgsConstructor
public class UsersForm {
    private Long id;
    private String userId; // 사용자 아이디
    private String password; // 비밀번호
    private String name;
    private int age;
    private String phoneNumber;
    private String email;
    private Integer classes = 1; // 변경: int에서 Integer로 변경

    public Users toEntity() {
        return new Users(id, userId, password, name, age, phoneNumber, email, classes != null ? classes : 1);
    }
}