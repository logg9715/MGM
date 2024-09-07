package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.CMCommentENT;
import com.example.MultiGreenMaster.repository.CMCommentREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CMCommentSRV {

    @Autowired
    private CMCommentREP cmCommentRepository; // CMCommentRepository 의존성 주입
    // 특정 게시글의 모든 댓글을 조회하는 메서드
    public List<CMCommentENT> findCommentsByPostId(Long postId) {
        return cmCommentRepository.findByCmPostId(postId);
    }

    // 댓글 좋아요 수를 증가시키는 메서드
    public void incrementLikeCount(Long id) {
        CMCommentENT comment = findCommentById(id);
        if (comment != null) {
            comment.incrementLikeCount();
            cmCommentRepository.save(comment);
        }
    }

    // ID로 댓글을 조회하는 메서드
    public CMCommentENT findCommentById(Long id) {
        return cmCommentRepository.findById(id).orElse(null);
    }

    // 댓글을 저장하는 메서드
    public void saveComment(CMCommentENT comment) {
        cmCommentRepository.save(comment);
    }

    // 댓글을 수정하는 메서드 추가
    public void updateComment(Long id, String content) {
        CMCommentENT comment = findCommentById(id);
        if (comment != null) {
            comment.setContent(content); // 댓글 내용 업데이트
            cmCommentRepository.save(comment); // 변경된 내용을 저장
        }
    }

    public List<CMCommentENT> deleteComment() {
        return cmCommentRepository.findByDisableFalse(); // 비활성화된 댓글 검색
    }
}
