package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.CMCommentENT;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CMCommentREP extends CrudRepository<CMCommentENT, Long> {
    // 특정 게시글의 모든 댓글을 조회하는 메서드
    List<CMCommentENT> findByCmPostId(Long cmPostId);

    // 특정 유저의 댓글을 작성일 기준 내림차순으로 조회
    List<CMCommentENT> findByUser_IdOrderByRegdateDesc(Long userId);

    // 특정 사용자의 댓글을 가져오는 메서드
    List<CMCommentENT> findByUser_Id(Long userId);

    // 댓글글을 비활성화 여부에 따라 조회
    List<CMCommentENT> findByDisableFalse();
}