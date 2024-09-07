package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.FreeBoard_CommentENT;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CMCommentREP extends CrudRepository<FreeBoard_CommentENT, Long> {
    // 특정 게시글의 모든 댓글을 조회하는 메서드
    List<FreeBoard_CommentENT> findByCmPostId(Long cmPostId);

    // 특정 유저의 댓글을 작성일 기준 내림차순으로 조회
    List<FreeBoard_CommentENT> findByUser_IdOrderByRegdateDesc(Long userId);

    // 특정 사용자의 댓글을 가져오는 메서드
    List<FreeBoard_CommentENT> findByUser_Id(Long userId);
}