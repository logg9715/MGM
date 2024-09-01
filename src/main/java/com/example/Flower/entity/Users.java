package com.example.Flower.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //이 코드 때문에 더미 파일에 id값을 빼었다. 에러가 나기 때문.
    private Long id;//고유번호

    @Column(name = "USERID", unique = true) //data.sql 에러 때문에 대문자로 수정
    private String userId;//유저 아이디

    @Column
    private String password;//비밀번호

    @Column
    private String name;//이름

    @Column
    private int age;//나이

    @Column(name = "PHONENUMBER", nullable = true) //데이터베이스 에러 때문에 수정
    private String phoneNumber;//전화번호

    @Column(name="email", nullable = true)
    private String email;//이메일

    @Column
    private int classes = 1;//등급. 0이 관리자, 1이 일반 유저이다. 기본값을 일반 사용자인 1로 준다.
}
