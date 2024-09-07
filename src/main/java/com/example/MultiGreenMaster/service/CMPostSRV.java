package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.CMPostENT;
import com.example.MultiGreenMaster.repository.CMPostREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CMPostSRV {

    @Autowired
    private CMPostREP cmPostRepository; // CMPostRepository 의존성 주입

    // 게시글 저장 메서드
    public void savePost(CMPostENT post) {
        cmPostRepository.save(post); // 게시글 저장
    }

    // 모든 게시글 조회 메서드
    public List<CMPostENT> findAllPosts() {
        return (List<CMPostENT>) cmPostRepository.findAll(); // 모든 게시글 조회
    }

    // ID로 게시글 조회 메서드
    public CMPostENT findPostById(Long id) {
        Optional<CMPostENT> post = cmPostRepository.findById(id); // ID로 게시글 조회
        return post.orElse(null); // 결과 반환
    }

    // 게시글 조회수 증가 메서드
    public void incrementCount(Long id) {
        CMPostENT post = findPostById(id);
        if (post != null) {
            post.incrementCount(); // 조회수 증가
            savePost(post); // 업데이트된 게시글 저장
        }
    }

    // 게시글 좋아요 수 증가 메서드
    public void incrementLikeCount(Long id) {
        CMPostENT post = findPostById(id);
        if (post != null) {
            post.incrementLikeCount(); // 좋아요 수 증가
            savePost(post); // 업데이트된 게시글 저장
        }
    }

    // 최신 4개의 게시글을 내림차순으로 조회하는 메서드
    public List<CMPostENT> findTop4PostsDesc() {
        return cmPostRepository.findTop4ByOrderByRegdateDesc(); // 내림차순으로 정렬
    }

    // 특정 사용자가 작성한 게시글을 내림차순으로 조회하는 메서드
    public List<CMPostENT> findPostsByUserId(Long userId) {
        return cmPostRepository.findByUserIdOrderByRegdateDesc(userId); // 사용자의 게시글을 내림차순으로 정렬하여 조회
    }
}
