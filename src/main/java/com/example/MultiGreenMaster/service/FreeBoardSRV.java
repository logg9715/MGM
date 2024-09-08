package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.FreeBoardENT;
import com.example.MultiGreenMaster.repository.FreeBoardPictureREP;
import com.example.MultiGreenMaster.repository.FreeBoardREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FreeBoardSRV {

    @Autowired
    private FreeBoardREP freeBoardREP; // CMPostRepository 의존성 주입

    @Autowired
    private FreeBoardPictureREP freeBoardPictureREP;  // CMPictureRepository 의존성 주입

    // 게시글 저장 메서드
    public void savePost(FreeBoardENT post) {
        freeBoardREP.save(post);  // 게시글 저장
        if (post.getPictures() != null) {
            freeBoardPictureREP.saveAll(post.getPictures());  // 관련된 사진도 함께 저장
        }
    }

    public List<FreeBoardENT> findAllPosts() {
        return freeBoardREP.findByDisableFalse();  // 비활성화 되지 않은 모든 게시글 조회
    }

    public FreeBoardENT findPostById(Long id) {
        Optional<FreeBoardENT> post = freeBoardREP.findById(id);  // ID로 게시글 조회
        return post.orElse(null);  // 결과 반환
    }

    public void incrementCount(Long id) {
        FreeBoardENT post = findPostById(id);
        if (post != null) {
            post.incrementCount();  // 조회수 증가
            savePost(post);  // 업데이트된 게시글 저장
        }
    }

    public void incrementLikeCount(Long id) {
        FreeBoardENT post = findPostById(id);
        if (post != null) {
            post.incrementLikeCount();  // 좋아요 수 증가
            savePost(post);  // 업데이트된 게시글 저장
        }
    }

    public List<FreeBoardENT> findTop4PostsDesc() {
        return freeBoardREP.findTop4ByOrderByRegdateDesc();  // 최신 게시글 4개 조회
    }

    public List<FreeBoardENT> findPostsByUserId(Long userId) {
        return freeBoardREP.findByUserIdOrderByRegdateDesc(userId);  // 사용자의 게시글 조회
    }
}
