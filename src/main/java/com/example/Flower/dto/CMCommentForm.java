package com.example.Flower.dto;


import com.example.Flower.entity.CMComment;
import com.example.Flower.entity.CMPost;
import com.example.Flower.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "cmPostId") // cmPostId 필드를 toString()에서 제외하여 순환 참조를 피함
public class CMCommentForm {
    private Long id; // 댓글 ID
    private CMPost cmPostId; // 게시글 ID
    private User userId; // 사용자 ID
    private String content; // 댓글 내용
    private int likeCount; // 댓글 좋아요
    private LocalDateTime regdate; // 작성 시간

    // 엔티티로 변환하는 메서드
    public CMComment toEntity() {
        return CMComment.builder()
                .id(id)
                .user(userId)
                .cmPost(cmPostId)
                .content(content)
                .likeCount(likeCount)
                .regdate(regdate)
                .build();
    }

}
