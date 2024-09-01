package com.example.Flower.service;

import com.example.Flower.entity.CMComment;
import com.example.Flower.repository.CMCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CMCommentService {

    @Autowired
    private CMCommentRepository cmCommentRepository; // CMCommentRepository 의존성 주입

    // 댓글 저장 메서드
    public void saveComment(CMComment comment) {
        cmCommentRepository.save(comment); // 댓글 저장
    }

    // 특정 게시글의 모든 댓글 조회 메서드
    public List<CMComment> findCommentsByPostId(Long postId) {
        return cmCommentRepository.findByCmPostId(postId); // 특정 게시글의 모든 댓글 조회
    }

    // ID로 댓글 조회 메서드
    public CMComment findCommentById(Long id) {
        Optional<CMComment> comment = cmCommentRepository.findById(id); // ID로 댓글 조회
        return comment.orElse(null); // 결과 반환
    }

    // 댓글 좋아요 수 증가 메서드
    public void incrementLikeCount(Long id) {
        CMComment comment = findCommentById(id);
        if (comment != null) {
            comment.incrementLikeCount(); // 좋아요 수 증가
            saveComment(comment); // 업데이트된 댓글 저장
        }
    }

    // 특정 유저의 모든 댓글을 내림차순으로 조회하는 메서드
    public List<CMComment> findCommentsByUserId(Long userId) {
        return cmCommentRepository.findByUser_IdOrderByRegdateDesc(userId); // 댓글 리스트 반환
    }
}
