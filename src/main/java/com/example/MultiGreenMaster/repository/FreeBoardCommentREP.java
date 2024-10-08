package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.FreeBoardCommentENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreeBoardCommentREP extends JpaRepository<FreeBoardCommentENT, Long> {
    // 특정 게시글의 모든 댓글을 조회하는 메서드
    List<FreeBoardCommentENT> findByCmPostId(Long cmPostId);

    // 특정 유저의 댓글을 작성일 기준 내림차순으로 조회
    @Query(value = "SELECT * FROM flower.freeboardcomment where user_id = :userId and disable = 1 ORDER BY regdate DESC LIMIT 3", nativeQuery = true)
    List<FreeBoardCommentENT> findRecentCommentsByUserId(@Param("userId") Long userId);

    // 특정 사용자의 댓글을 가져오는 메서드
    List<FreeBoardCommentENT> findByUser_Id(Long userId);

    // 댓글글을 비활성화 여부에 따라 조회
    List<FreeBoardCommentENT> findByDisableFalse();
}