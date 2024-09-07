package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.Diary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findTop4ByOrderByRegdateDesc(); // 내림차순 정렬
    List<Diary> findAll(Sort regdate); // 모든 다이어리 조회

    // 특정 사용자가 작성한 게시글을 내림차순으로 조회하는 쿼리 메서드
    List<Diary> findByUserIdOrderByRegdateDesc(Long userId);

    // 게시글을 비활성화 여부에 따라 조회
    List<Diary> findByDisableFalse();
}
