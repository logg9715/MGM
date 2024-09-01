package com.example.Flower.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime regdate;
    private int likeCount;
    private String type; // "comment" 또는 "recomment"
}