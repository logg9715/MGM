package com.example.Flower.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class CMRecomment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 대댓글 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cm_comment_id")
    @JsonBackReference // 순환 참조 방지
    private CMComment cmComment; // 댓글 정보 가져오기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저 정보 가져오기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cm_post_id", nullable = false) // cm_post_id로 외래키 설정
    @JsonBackReference // 순환 참조 방지
    private CMPost cmPost; // 게시글 정보 가져오기

    @Column
    private String content; // 대댓글 내용

    @Column
    private int likeCount; // 대댓글 좋아요

    @Column
    private LocalDateTime regdate; // 대댓글 작성 시간

    // 좋아요 수 증가 메서드
    public void incrementLikeCount() {
        this.likeCount++;
    }

    @PrePersist
    protected void onCreate() {
        this.regdate = LocalDateTime.now(); // 현재 시간을 regdate에 설정
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}