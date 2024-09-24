package com.example.MultiGreenMaster.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {"user", "comments", "pictures"}) // 순환 참조 방지
@Entity
//@Table(name = "cm_post")
@Table(name = "freeboard")
public class FreeBoardENT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserENT user; // 유저 정보 가져오기

    @Column
    private String title; // 게시글 제목

    @Column(name = "content", nullable = true) // "content"라는 이름의 열을 정의하고 null 값을 허용
    private String content; // 게시글 내용

    /* @Column(name = "like_count")
    private int likeCount; // 좋아요 갯수 */

    @Column
    private LocalDateTime regdate; // 글 작성시간

    /*
    @PrePersist
    protected void onCreate() {
        this.regdate = LocalDateTime.now(); // 현재 시간을 regdate에 설정
    }
     */

    @Column
    private int count; // 조회수

    @Column
    private boolean disable; // 비활성화 여부

    // 사진과의 관계 설정
    @OneToMany(mappedBy = "cmPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference // 순환 참조 방지
    private List<FreeBoardPictureENT> pictures;  // 게시글에 포함된 사진 목록

    // 댓글과의 관계 설정
    @OneToMany(mappedBy = "cmPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // 순환 참조 방지
    private List<FreeBoardCommentENT> comments;  // 게시글에 달린 댓글 목록

    // 조회수 증가 메서드
    public void incrementCount() {
        this.count++;
    }

    // 좋아요 수 증가 메서드
    /*public void incrementLikeCount() {
        this.likeCount++;
    }*/

    public void setTimeNow() {
        this.regdate = LocalDateTime.now().withNano(0);
    }
}


