package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.DiaryBoardENT;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryBoardREP extends JpaRepository<DiaryBoardENT, Long> {
    List<DiaryBoardENT> findAll(Sort regdate); // 모든 다이어리 조회

    /*
    // 특정 사용자가 작성한 게시글을 내림차순으로 조회하는 쿼리 메서드
    List<DiaryBoardENT> findByUserIdOrderByRegdateDesc(Long userId);
    */
}
