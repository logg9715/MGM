package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.FreeBoard_CommentENT;
import com.example.MultiGreenMaster.repository.FreeBoard_CommentREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FreeBoard_CommentSRV {

    @Autowired
    private FreeBoard_CommentREP cmCommentRepository; // CMCommentRepository 의존성 주입

    // 댓글 저장 메서드
    public void saveComment(FreeBoard_CommentENT comment) {
        cmCommentRepository.save(comment); // 댓글 저장
    }

    // 특정 게시글의 모든 댓글 조회 메서드
    public List<FreeBoard_CommentENT> findCommentsByPostId(Long postId) {
        return cmCommentRepository.findByCmPostId(postId); // 특정 게시글의 모든 댓글 조회
    }

    // ID로 댓글 조회 메서드
    public FreeBoard_CommentENT findCommentById(Long id) {
        Optional<FreeBoard_CommentENT> comment = cmCommentRepository.findById(id); // ID로 댓글 조회
        return comment.orElse(null); // 결과 반환
    }

    // 댓글 좋아요 수 증가 메서드
    public void incrementLikeCount(Long id) {
        FreeBoard_CommentENT comment = findCommentById(id);
        if (comment != null) {
            comment.incrementLikeCount(); // 좋아요 수 증가
            saveComment(comment); // 업데이트된 댓글 저장
        }
    }

    // 특정 유저의 모든 댓글을 내림차순으로 조회하는 메서드
    public List<FreeBoard_CommentENT> findCommentsByUserId(Long userId) {
        return cmCommentRepository.findByUser_IdOrderByRegdateDesc(userId); // 댓글 리스트 반환
    }
}
