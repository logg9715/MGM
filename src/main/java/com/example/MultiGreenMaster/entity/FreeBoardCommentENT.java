package com.example.MultiGreenMaster.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Table(name = "freeboardcomment")
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {"user", "cmPost", "recomments"}) // users, cmPost, recomments 필드를 toString()에서 제외하여 순환 참조를 피함
@Entity
public class FreeBoardCommentENT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserENT user; // 유저 정보 가져오기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cmPost_id")
    @JsonBackReference // 순환 참조를 방지하기 위해 사용
    private FreeBoardENT cmPost; // 게시글 정보 가져오기

    @Column(name = "content")
    private String content; // 댓글 내용

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime regdate; // 댓글 작성 시간

    @Column
    private boolean disable; // 비활성화 여부

    /*
    @PrePersist
    protected void onCreate() {
        this.regdate = LocalDateTime.now(); // 현재 시간을 regdate에 설정
    }
     */

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // 순환 참조를 방지하기 위해 사용
    private List<FreeBoardCommentENT> recomments; // 대댓글 목록 가져오기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    @JsonBackReference // 순환 참조를 방지하기 위해 사용
    private FreeBoardCommentENT parentComment; // 부모 댓글 참조

    // 유저 ID 가져오기
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    // 부모 댓글 ID 가져오기
    public Long getParentCommentId() {
        return parentComment != null ? parentComment.getId() : null;
    }

    @Setter
    @Getter
    @Transient
    private boolean canEdit;  // 수정 가능 여부

    public void setTimeNow() {
        this.regdate = LocalDateTime.now().withNano(0);
    }
}
