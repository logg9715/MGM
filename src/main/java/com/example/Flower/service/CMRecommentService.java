package com.example.Flower.service;

import com.example.Flower.entity.CMRecomment;
import com.example.Flower.repository.CMRecommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CMRecommentService {

    @Autowired
    private CMRecommentRepository cmRecommentRepository; // CMRecommentRepository 의존성 주입

    // 대댓글 저장 메서드
    public void saveRecomment(CMRecomment recomment) {
        cmRecommentRepository.save(recomment); // 대댓글 저장
    }

    // 특정 댓글의 모든 대댓글 조회 메서드
    public List<CMRecomment> findRecommentsByCommentId(Long commentId) {
        return cmRecommentRepository.findByCmCommentId(commentId); // 특정 댓글의 모든 대댓글 조회
    }

    // ID로 대댓글 조회 메서드
    public CMRecomment findRecommentById(Long id) {
        Optional<CMRecomment> recomment = cmRecommentRepository.findById(id);
        return recomment.orElse(null);
    }

    // 대댓글 좋아요 수 증가 메서드
    public void incrementLikeCount(Long id) {
        CMRecomment recomment = findRecommentById(id);
        if (recomment != null) {
            recomment.incrementLikeCount(); // 좋아요 수 증가
            saveRecomment(recomment); // 업데이트된 대댓글 저장
        }
    }

    // 특정 유저의 댓글과 대댓글을 통합하여 조회하는 메서드
    public List<Object[]> findCommentsAndRecommentsByUserId(Long userId) {
        return cmRecommentRepository.findCommentsAndRecommentsByUserId(userId);
    }
}
