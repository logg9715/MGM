package com.example.Flower.api;

import com.example.Flower.controller.SessionCheckController;
import com.example.Flower.dto.CMGetpostForm;
import com.example.Flower.dto.CMPostForm;
import com.example.Flower.entity.CMPost;
import com.example.Flower.entity.User;
import com.example.Flower.service.CMPostService;
import com.example.Flower.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts") // "/api/posts" 경로와 매핑
public class CMPostApiController extends SessionCheckController {

    private static final Logger logger = LoggerFactory.getLogger(CMPostApiController.class); // 로그 설정

    @Autowired
    private CMPostService cmPostService; // CMPostService 의존성 주입

    @Autowired
    private UserService userService; // UserService 의존성 주입

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/new") // POST 요청을 "/new" 경로와 매핑
    public ResponseEntity<String> createPost(@ModelAttribute CMPostForm form, HttpSession session) {
        logger.info("Request to create new post: {}", form); // 새 게시글 생성 요청

        Long userId = (Long) session.getAttribute("userId"); // 세션에서 userId 추출
        User loginUser = userService.getLoginUserById(userId); // 로그인된 사용자 조회
        if (loginUser == null) {
            logger.error("Logged in user not found."); // 사용자 조회 실패 시 로그 출력
            return ResponseEntity.badRequest().body("User not found"); // 오류 응답 반환
        }

        List<byte[]> pictureBytesList = form.getPictures().stream() // 사진 파일을 바이트 배열로 변환
                .map(picture -> {
                    try {
                        return picture.getBytes(); // 사진을 바이트 배열로 변환
                    } catch (IOException e) {
                        logger.error("Error reading picture bytes: {}", e.getMessage()); // 오류 발생 시 로그 출력
                        return null;
                    }
                })
                .collect(Collectors.toList()); // 변환된 바이트 배열 리스트로 수집

        CMPost post = form.toEntity(pictureBytesList); // CMPostForm을 CMPost 엔티티로 변환
        post.setUser(loginUser); // 로그인한 사용자 설정
        cmPostService.savePost(post); // 게시글 저장
        logger.info("Post saved successfully: {}", post); // 게시글 저장 완료 로그 출력

        return ResponseEntity.ok("Post created successfully"); // 성공 응답 반환
    }

    @GetMapping // GET 요청을 기본 경로와 매핑
    public ResponseEntity<List<CMGetpostForm>> listPosts() {
        logger.info("Requesting post list"); // 게시글 목록 요청
        List<CMPost> posts = cmPostService.findAllPosts(); // 모든 게시글 조회
        List<CMGetpostForm> postForms = posts.stream().map(post -> new CMGetpostForm(
                post.getId(),
                post.getUser(),
                post.getTitle(),
                post.getLikeCount(),
                post.getRegdate(),
                post.getCount()
        )).collect(Collectors.toList()); // 변환된 게시글 리스트로 수집
        logger.info("Post list retrieved successfully"); // 게시글 목록 조회 완료 로그 출력
        return ResponseEntity.ok(postForms); // 조회된 게시글 목록 반환
    }

    @GetMapping("/{id}") // GET 요청을 "/{id}" 경로와 매핑
    public ResponseEntity<CMPostForm> getPost(@PathVariable Long id) {
        logger.info("Requesting post detail: Post ID {}", id); // 게시글 상세 조회 요청
        CMPost post = cmPostService.findPostById(id); // ID로 게시글 조회
        if (post == null) {
            return ResponseEntity.notFound().build(); // 게시글을 찾을 수 없는 경우 404 응답 반환
        }

        List<String> pictureBase64List = post.getPictures() != null ? post.getPictures().stream()
                .map(Base64.getEncoder()::encodeToString).collect(Collectors.toList()) : null; // 사진을 Base64로 인코딩

        CMPostForm postForm = new CMPostForm(
                post.getId(),
                post.getUser(),
                post.getTitle(),
                post.getContent(),
                pictureBase64List,
                post.getLikeCount(),
                post.getRegdate(),
                post.getCount()
        );

        logger.info("Post detail retrieved successfully: Post ID {}", id); // 게시글 상세 조회 완료 로그 출력
        return ResponseEntity.ok(postForm); // 조회된 게시글 반환
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/{id}/like") // POST 요청을 "/{id}/like" 경로와 매핑
    public ResponseEntity<Integer> likePost(@PathVariable Long id) {
        logger.info("Request to increase like count: Post ID {}", id); // 게시글 좋아요 증가 요청
        cmPostService.incrementLikeCount(id); // 게시글의 좋아요 수 증가
        CMPost post = cmPostService.findPostById(id); // ID로 게시글 조회
        return post != null ? ResponseEntity.ok(post.getLikeCount()) : ResponseEntity.notFound().build(); // 게시글이 존재할 경우 좋아요 수 반환, 그렇지 않으면 404 응답
    }

    // 최신 4개의 게시글을 내림차순으로 반환
    @GetMapping("/recent")
    public List<CMPostForm> getRecentPosts() {
        List<CMPost> posts = cmPostService.findTop4PostsDesc(); // 내림차순으로 정렬된 게시글 조회
        return posts.stream().map(post -> {
            List<String> pictureBase64List = post.getPictures() != null ? post.getPictures().stream()
                    .map(Base64.getEncoder()::encodeToString).collect(Collectors.toList()) : null;
            return new CMPostForm(
                    post.getId(),
                    post.getUser(),
                    post.getTitle(),
                    post.getContent(),
                    pictureBase64List,
                    post.getLikeCount(),
                    post.getRegdate(),
                    post.getCount()
            );
        }).collect(Collectors.toList());
    }

    // 특정 사용자가 작성한 게시글을 내림차순으로 반환
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<CMPostForm>> getPostsByUser(@PathVariable Long userId) {
        logger.info("Requesting posts by user: User ID {}", userId); // 사용자의 게시글 요청
        List<CMPost> posts = cmPostService.findPostsByUserId(userId); // 사용자의 게시글 조회
        List<CMPostForm> postForms = posts.stream().map(post -> { // 각 게시글을 CMPostForm으로 변환
            List<String> pictureBase64List = post.getPictures() != null ? post.getPictures().stream()
                    .map(Base64.getEncoder()::encodeToString).collect(Collectors.toList()) : null; // 사진을 Base64로 인코딩
            return new CMPostForm(
                    post.getId(),
                    post.getUser(),
                    post.getTitle(),
                    post.getContent(),
                    pictureBase64List,
                    post.getLikeCount(),
                    post.getRegdate(),
                    post.getCount()
            );
        }).collect(Collectors.toList()); // 변환된 게시글 리스트로 수집
        logger.info("Posts by user retrieved successfully: User ID {}", userId); // 사용자의 게시글 조회 완료
        return ResponseEntity.ok(postForms); // 조회된 게시글 목록 반환
    }
}
