package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.CMPicture;
import com.example.MultiGreenMaster.entity.CMPostENT;
import com.example.MultiGreenMaster.entity.UserENT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CMPostFRM {
    private Long id; // 게시글 ID
    private UserENT user; // 사용자 객체
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private List<MultipartFile> pictures; // 사진 리스트 (파일 형식)
    private List<String> pictureBase64List; // 사진 리스트 (Base64 인코딩 형식)
    private int likeCount; // 좋아요 갯수
    private LocalDateTime regdate; // 작성 시간
    private int count; // 조회수

    // 생성자 - MultipartFile을 포함하지 않음
    public CMPostFRM(Long id, UserENT user, String title, String content, List<String> pictureBase64List, int likeCount, LocalDateTime regdate, int count) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.pictureBase64List = pictureBase64List;
        this.likeCount = likeCount;
        this.regdate = regdate;
        this.count = count;
    }

    // 엔티티로 변환하는 메서드
    public CMPostENT toEntity() {
        List<CMPicture> pictures = this.pictures.stream()
                .map(multipartFile -> {
                    try {
                        return CMPicture.builder()
                                .pictureData(multipartFile.getBytes())
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to convert MultipartFile to byte[]", e);
                    }
                })
                .collect(Collectors.toList());

        CMPostENT post = CMPostENT.builder()
                .id(this.id)
                .user(this.user)
                .title(this.title)
                .content(this.content)
                .pictures(pictures)
                .likeCount(this.likeCount)
                .regdate(this.regdate != null ? this.regdate : LocalDateTime.now())
                .count(this.count)
                .disable(false)
                .build();

        pictures.forEach(picture -> picture.setCmPost(post));
        return post;
    }
}

