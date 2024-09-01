package com.example.Flower.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class UsersPlant {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) //이 코드 때문에 더미 파일에 id값을 빼었다. 에러가 나기 때문.
    private Long id;//유저 보유 식물 고유 번호

    @Column(nullable = false) // NOT NULL 추가
    private String name;//유저 식물 이름

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false) // 외래 키로 Users 테이블의 id를 참조
    private User user; // FK(유저 정보 테이블) 유저 고유 코드

    @ManyToOne
    @JoinColumn(name = "plants_id", nullable = false) // 외래 키로 Plants 테이블의 id를 참조
    private Plants plants; // FK(식물 정보 테이블) 식물 고유 코드

    @Column
    private LocalDateTime regdate;//식물 배분일
    @PrePersist
    protected void onCreate() {
        this.regdate = LocalDateTime.now();
    }

    @Column
    private LocalDateTime elapsed;//식물 경과시간

    @Column(name = "picture", columnDefinition="LONGBLOB")
    private byte[] picture;//식물 현재 사진

    @Column(nullable = true)
    private int watered_count;//물을 준 횟수

    @Column(nullable = true)
    private int fertilizered_count;//비료를 준 횟수

    @Column(nullable = true)
    private float humidity;//습도

    @Column(nullable = true)
    private float temperature;//온도
}
