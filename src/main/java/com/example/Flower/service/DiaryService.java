package com.example.Flower.service;

import com.example.Flower.entity.Diary;
import com.example.Flower.entity.Diary;
import com.example.Flower.repository.DiaryRepository;
import com.example.Flower.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository; // diaryRepository 의존성 주입

    // 게시글 저장 메서드
    public void savePost(Diary diary) {
        diaryRepository.save(diary); // 게시글 저장
    }

    // 모든 게시글 조회 메서드
    public List<Diary> findAllPosts() {
        return (List<Diary>) diaryRepository.findAll(); // 모든 게시글 조회
    }

    // ID로 게시글 조회 메서드
    public Diary findPostById(Long id) {
        Optional<Diary> post = diaryRepository.findById(id); // ID로 게시글 조회
        return post.orElse(null); // 결과 반환
    }

    // 게시글 조회수 증가 메서드
    public void incrementCount(Long id) {
        Diary post = findPostById(id);
        if (post != null) {
            post.incrementCount(); // 조회수 증가
            savePost(post); // 업데이트된 게시글 저장
        }
    }

    // 게시글 좋아요 수 증가 메서드
    public void incrementLikeCount(Long id) {
        Diary post = findPostById(id);
        if (post != null) {
            post.incrementLikeCount(); // 좋아요 수 증가
            savePost(post); // 업데이트된 게시글 저장
        }
    }

    // 최신 4개의 게시글을 내림차순으로 조회하는 메서드
    public List<Diary> findTop4PostsDesc() {
        return diaryRepository.findTop4ByOrderByRegdateDesc(); // 내림차순으로 정렬
    }

    // 특정 사용자가 작성한 게시글을 내림차순으로 조회하는 메서드
    public List<Diary> findPostsByUserId(Long userId) {
        return diaryRepository.findByUserIdOrderByRegdateDesc(userId); // 사용자의 게시글을 내림차순으로 정렬하여 조회
    }
}
