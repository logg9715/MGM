package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.DiaryBoardENT;
import com.example.MultiGreenMaster.exeption.DiaryDeleteExcption;
import com.example.MultiGreenMaster.exeption.DiaryNotFoundException;
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

    // ID로 게시글 조회 메서드
    public DiaryBoardENT findDiaryById(Long id) {
        Optional<DiaryBoardENT> diary = diaryRepository.findById(id);  // ID로 게시글 조회
        return diary.orElse(null);  // null을 반환하여 존재하지 않는 경우 처리
    }


    // 사용자의 다이어리와 공개 여부, 비활성화 여부에 따른 다이어리 조회 메서드 (친구 관련 기능 없음)
    public List<DiaryBoardENT> findDiariesForUser(Long userId) {
        return diaryRepository.findAll().stream()
                .filter(diary ->
                        !diary.isDisable() &&  // 비활성화되지 않은 다이어리만 필터링
                                (diary.getIsPublic() == 1 ||  // 전체 공개
                                        diary.getUser().getId().equals(userId)))  // 사용자 본인의 다이어리만 필터링
                .collect(Collectors.toList());
    }

}
