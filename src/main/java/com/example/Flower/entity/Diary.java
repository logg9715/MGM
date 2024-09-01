package com.example.Flower.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "user") // users 필드를 toString()에서 제외하여 순환 참조를 피함
@Entity
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저 정보 가져오기

    @Column
    private String title; // 게시글 제목

    @Column(name = "content", nullable = true) // "content"라는 이름의 열을 정의하고 null 값을 허용
    private String content; // 게시글 내용

    @ElementCollection
    @Column(name = "pictures", columnDefinition = "LONGBLOB") // "pictures"라는 이름의 열을 정의하고 데이터 타입을 LONGBLOB으로 설정
    private List<byte[]> pictures; // 사진 리스트

    @Column(name = "like_count")
    private int likeCount; // 좋아요 갯수

    @Column
    private LocalDateTime regdate; // 글 작성시간

    @PrePersist
    protected void onCreate() {
        this.regdate = LocalDateTime.now(); // 현재 시간을 regdate에 설정
    }

    @Column
    private int count; // 조회수

    // 조회수 증가 메서드
    public void incrementCount() {
        this.count++;
    }

    // 좋아요 수 증가 메서드
    public void incrementLikeCount() {
        this.likeCount++;
    }
}
