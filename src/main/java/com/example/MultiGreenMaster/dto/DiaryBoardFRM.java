package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.DiaryBoardENT;
import com.example.MultiGreenMaster.entity.UserENT;
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
public class DiaryBoardFRM {
    private Long id; // 다이어리 ID
    private UserENT user; // 사용자 객체
    private String title; // 다이어리 제목
    private String content; // 다이어리 내용
    private List<MultipartFile> pictures; // 사진 리스트 (파일 형식)
    private List<String> pictureBase64List; // 사진 리스트 (Base64 인코딩 형식)
    private LocalDateTime regdate; // 작성 시간
    private Long isPublic; // 0 = 비공개, 1 = 전체 공개, 2 = 친구에게만 공개

    // 기본 생성자 이외에 특정 필드들을 초기화하는 생성자
    public DiaryBoardFRM(Long id, UserENT user, String title, String content, List<String> pictureBase64List, LocalDateTime regdate, Long isPublic) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.pictureBase64List = pictureBase64List;
        this.regdate = regdate;
        this.isPublic = isPublic;
    }

    // 엔티티로 변환하는 메서드
    public DiaryBoardENT toEntity(List<byte[]> pictureBytesList) {
        return DiaryBoardENT.builder()
                .id(this.id)
                .user(this.user)
                .title(this.title)
                .content(this.content)
                .pictures(pictureBytesList)
                .regdate(this.regdate != null ? this.regdate : LocalDateTime.now()) // 기본값 설정
                .isPublic(this.isPublic)
                .disable(false)
                .build();
    }
}