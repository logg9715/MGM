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
@ToString(exclude = {"user", "cmPost", "recomments"}) // users, cmPost, recomments 필드를 toString()에서 제외하여 순환 참조를 피함
@Entity
public class CMComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저 정보 가져오기

    @ManyToOne
    @JoinColumn(name = "cmPost_id")
    private CMPost cmPost; // 게시글 정보 가져오기

    @Column(name = "content")
    private String content; // 댓글 내용

    @Column
    private int likeCount; // 댓글 좋아요

    @Column
    private LocalDateTime regdate; // 댓글 작성 시간

    @PrePersist
    protected void onCreate() {
        this.regdate = LocalDateTime.now(); // 현재 시간을 regdate에 설정
    }

    // 좋아요 수 증가 메서드
    public void incrementLikeCount() {
        this.likeCount++;
    }

    @OneToMany(mappedBy = "cmComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CMRecomment> recomments; // 대댓글 목록 가져오기

    public CMComment(Long id, User user, CMPost cmPost, String content, int likeCount, LocalDateTime regdate) {
        this.id = id;
        this.user = user;
        this.cmPost = cmPost;
        this.content = content;
        this.likeCount = likeCount;
        this.regdate = regdate;
    }
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}
