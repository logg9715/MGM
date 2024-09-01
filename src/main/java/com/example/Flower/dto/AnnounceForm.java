package com.example.Flower.dto;

import com.example.Flower.entity.Announce;
import com.example.Flower.entity.User;
import com.example.Flower.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class AnnounceForm {
    private Long id;
    private String title; // 제목을 받을 필드
    private String content; // 내용을 받을 필드

    public Announce toEntity() {
        return Announce.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .build();
    }



}
