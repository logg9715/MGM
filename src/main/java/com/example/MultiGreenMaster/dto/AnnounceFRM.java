package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.AnnounceENT;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class AnnounceFRM {
    private Long id;
    private String title; // 제목을 받을 필드
    private String content; // 내용을 받을 필드

    public AnnounceENT toEntity() {
        return AnnounceENT.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .disable(false)
                .build();
    }



}
