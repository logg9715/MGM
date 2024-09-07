package com.example.MultiGreenMaster.dto;


import com.example.MultiGreenMaster.entity.CMCommentENT;
import com.example.MultiGreenMaster.entity.CMPostENT;
import com.example.MultiGreenMaster.entity.UserENT;
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
public class CMCommentFRM {
    private Long id; // 댓글 ID
    private CMPostENT cmPostId; // 게시글 ID
    private UserENT userId; // 사용자 ID
    private String content; // 댓글 내용
    private int likeCount; // 댓글 좋아요
    private LocalDateTime regdate; // 작성 시간
    private Long parentCommentId; // 부모 댓글 ID

    // 엔티티로 변환하는 메서드
    public CMCommentENT toEntity() {
        CMCommentENT comment = CMCommentENT.builder()
                .id(id)
                .user(userId)
                .cmPost(cmPostId)
                .content(content)
                .likeCount(likeCount)
                .regdate(regdate)
                .disable(false)
                .build();

        if (parentCommentId != null) {
            CMCommentENT parent = new CMCommentENT();
            parent.setId(parentCommentId);
            comment.setParentComment(parent); // 부모 댓글 설정
        }

        return comment;
    }
}
