package com.example.MultiGreenMaster.dto;


import com.example.MultiGreenMaster.entity.FreeBoardCommentENT;
import com.example.MultiGreenMaster.entity.FreeBoardENT;
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
public class FreeBoardCommentFRM {
    private Long id; // 댓글 ID
    private FreeBoardENT cmPostId; // 게시글 ID
    private UserENT userId; // 사용자 ID
    private String content; // 댓글 내용
    private LocalDateTime regdate; // 작성 시간
    private Long parentCommentId; // 부모 댓글 ID

    // 엔티티로 변환하는 메서드
    public FreeBoardCommentENT toEntity() {
        FreeBoardCommentENT comment = FreeBoardCommentENT.builder()
                .id(id)
                .user(userId)
                .cmPost(cmPostId)
                .content(content)
                .regdate(regdate)
                .disable(false)
                .build();

        if (parentCommentId != null) {
            FreeBoardCommentENT parent = new FreeBoardCommentENT();
            parent.setId(parentCommentId);
            comment.setParentComment(parent); // 부모 댓글 설정
        }

        return comment;
    }
}
