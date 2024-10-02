package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.DiaryBoardENT;
import com.example.MultiGreenMaster.entity.UserENT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private boolean disable; // 글 삭제 여부 (true 일때 삭제)

    // 생성자 - MultipartFile을 포함하지 않는 경우
    public DiaryBoardFRM(Long id, UserENT user, String title, String content, List<String> pictureBase64List, LocalDateTime regdate, Boolean disable) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.pictureBase64List = pictureBase64List;
        this.regdate = regdate;
        this.disable = disable;
    }

    // 엔티티로 변환하는 메서드 (MultipartFile -> byte[] 변환)
    public DiaryBoardENT toEntity() {
        // 사진 파일을 byte[]로 변환
        List<byte[]> pictureBytesList = Optional.ofNullable(this.pictures)
                .orElse(Collections.emptyList()).stream()
                .map(multipartFile -> {
                    try {
                        return multipartFile.getBytes();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to convert MultipartFile to byte[]", e);
                    }
                })
                .collect(Collectors.toList());

        // DiaryBoardENT 엔티티 생성 및 반환
        return DiaryBoardENT.builder()
                .id(this.id)
                .user(this.user)
                .title(this.title)
                .content(this.content)
                .pictures(pictureBytesList)  // 변환된 사진 데이터 설정
                .regdate(this.regdate != null ? this.regdate : LocalDateTime.now()) // 작성 시간 설정
                .disable(false)  // 기본값으로 삭제 안된 상태 설정
                .build();
    }
}
