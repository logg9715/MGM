package com.example.MultiGreenMaster.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "diaryboard")
@Getter
@Setter
@Builder
@ToString(exclude = "user") // user 필드를 toString()에서 제외하여 순환 참조를 피함
@Entity
public class DiaryBoardENT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 다이어리 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserENT user; // 유저 정보

    @Column(nullable = false)
    private String title; // 다이어리 제목

    @Column(name = "content", nullable = true)
    private String content; // 다이어리 내용

    @ElementCollection
    @Column(name = "pictures", columnDefinition = "LONGBLOB")
    private List<byte[]> pictures; // 사진 리스트

    @Column(nullable = false)
    private LocalDateTime regdate; // 작성 시간

    @Column(nullable = false)
    private boolean disable; // 비활성화 여부

    @Builder.Default
    @Column(nullable = false)
    private int isPublic = 0; // 0 = 비공개, 1 = 전체 공개, 2 = 친구에게만 공개

    @PrePersist
    protected void onCreate() {
        this.regdate = LocalDateTime.now(); // 작성 시간을 현재 시간으로 설정
    }

    // 다이어리 공개 여부를 설정하는 메서드
    public void setPublic(int isPublic) {
        this.isPublic = isPublic;
    }
}
