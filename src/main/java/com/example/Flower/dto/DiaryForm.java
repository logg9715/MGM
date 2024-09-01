package com.example.Flower.dto;


import com.example.Flower.entity.CMPost;
import com.example.Flower.entity.Diary;
import com.example.Flower.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiaryForm {
    private Long id; // 게시글 ID
    private User user; // 사용자 객체
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private List<MultipartFile> pictures; // 사진 리스트 (파일 형식)
    private List<String> pictureBase64List; // 사진 리스트 (Base64 인코딩 형식)
    private int likeCount; // 좋아요 갯수
    private LocalDateTime regdate; // 작성 시간
    private int count; // 조회수

    // 기본 생성자 이외에 특정 필드들을 초기화하는 생성자
    public DiaryForm(Long id, User user, String title, String content, List<String> pictureBase64List, int likeCount, LocalDateTime regdate, int count) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.pictureBase64List = pictureBase64List;
        this.likeCount = likeCount;
        this.regdate = regdate;
        this.count = count;
    }

    // 엔티티로 변환하는 메서드, pictureBytesList는 List<byte[]> 형식의 이미지 데이터를 받음
    public Diary toEntity(List<byte[]> pictureBytesList) {
        return Diary.builder()
                .id(this.id)
                .user(this.user)
                .title(this.title)
                .content(this.content)
                .pictures(pictureBytesList)
                .likeCount(this.likeCount)
                .regdate(this.regdate != null ? this.regdate : LocalDateTime.now()) // 기본값 설정
                .count(this.count)
                .build();
    }
}
