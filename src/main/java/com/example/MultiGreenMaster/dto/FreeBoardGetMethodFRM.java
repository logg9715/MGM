package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.UserENT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FreeBoardGetMethodFRM {
    private Long id; // 게시글 ID
    private UserENT user; // 사용자 객체
    private String title; // 게시글 제목
    //private int likeCount; // 좋아요 갯수
    private LocalDateTime regdate; // 작성 시간
    private int count; // 조회수
}
