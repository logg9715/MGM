package com.example.Flower.dto;

import com.example.Flower.entity.CMComment;
import com.example.Flower.entity.CMRecomment;
import com.example.Flower.entity.CMPost; // CMPost import 추가
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
@ToString(exclude = "cmCommentId") // cmCommentId 필드를 toString()에서 제외하여 순환 참조를 피함
public class CMRecommentForm {
    private Long id; // 대댓글 ID
    private CMComment cmCommentId; // 댓글 ID
    private User userId; // 사용자 ID
    private CMPost cmPostId; // 게시글 ID 추가
    private String content; // 대댓글 내용
    private int likeCount; // 대댓글 좋아요
    private LocalDateTime regdate; // 작성 시간

    // 엔티티로 변환하는 메서드
    public CMRecomment toEntity(User loginUser) {
        return new CMRecomment(id, cmCommentId, userId, cmPostId, content, likeCount, regdate);
    }
}
