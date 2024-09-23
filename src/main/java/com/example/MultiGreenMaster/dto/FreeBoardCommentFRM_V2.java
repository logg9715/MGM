package com.example.MultiGreenMaster.dto;

import lombok.*;

import java.time.LocalDateTime;


/* 최근 3개 댓글 전송용 form */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "cmPostId") // cmPostId 필드를 toString()에서 제외하여 순환 참조를 피함
public class FreeBoardCommentFRM_V2 {
    private Long id; // 댓글 ID
    private Long cmPostId; // 게시글 ID
    private String content; // 댓글 내용
    private LocalDateTime regdate; // 작성 시간
}
