package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.DiaryBoardENT;
import com.example.MultiGreenMaster.repository.DiaryBoardREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiaryBoardSRV {

    @Autowired
    private DiaryBoardREP diaryRepository; // diaryRepository 의존성 주입

    // 게시글 저장 메서드
    public void saveDiary(DiaryBoardENT diary) {
        diaryRepository.save(diary); // 게시글 저장
    }

    // 모든 게시글 조회 메서드
    public List<DiaryBoardENT> findAllDiaries() {
        return diaryRepository.findAll(); // 모든 게시글 조회
    }

    // ID로 게시글 조회 메서드
    public DiaryBoardENT findDiaryById(Long id) {
        Optional<DiaryBoardENT> diary = diaryRepository.findById(id); // ID로 게시글 조회
        return diary.orElse(null); // 결과 반환
    }

    // 최신 4개의 게시글을 내림차순으로 조회하는 메서드
    public List<DiaryBoardENT> findTop4DiariesDesc() {
        return diaryRepository.findTop4ByOrderByRegdateDesc(); // 내림차순으로 정렬
    }

    // 특정 사용자가 작성한 게시글을 내림차순으로 조회하는 메서드
    public List<DiaryBoardENT> findDiariesByUserId(Long userId) {
        return diaryRepository.findByUserIdOrderByRegdateDesc(userId); // 사용자의 게시글을 내림차순으로 정렬하여 조회
    }

    // 비활성화되지 않은 게시글만 조회하는 메서드
    public List<DiaryBoardENT> findActiveDiaries() {
        return diaryRepository.findByDisableFalse(); // 비활성화되지 않은 게시글 조회
    }

    // 사용자의 다이어리와 공개 여부에 따른 다이어리 조회 메서드 (친구 관련 기능 없음)
    public List<DiaryBoardENT> findDiariesForUser(Long userId) {
        return diaryRepository.findAll().stream()
                .filter(diary ->
                        diary.getIsPublic() == 1L ||  // 전체 공개
                                diary.getUser().getId().equals(userId))  // 사용자 본인의 다이어리만 필터링
                .collect(Collectors.toList());
    }

    // 게시글 공개 여부 설정 메서드
    public void setDiaryPublic(Long id, Long isPublic) {
        DiaryBoardENT diary = findDiaryById(id);
        if (diary != null) {
            diary.setPublic(isPublic); // 공개 여부 설정
            saveDiary(diary); // 업데이트된 게시글 저장
        }
    }
}
