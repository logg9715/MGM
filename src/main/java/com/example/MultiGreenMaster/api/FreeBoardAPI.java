package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.controller.SessionCheckCTL;
import com.example.MultiGreenMaster.dto.FreeBoardGetMethodFRM;
import com.example.MultiGreenMaster.dto.FreeBoardFRM;
import com.example.MultiGreenMaster.entity.FreeBoardPictureENT;
import com.example.MultiGreenMaster.entity.FreeBoardENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.FreeBoardGoodSRV;
import com.example.MultiGreenMaster.service.FreeBoardSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/freeboard") // "/api/posts" 경로와 매핑
public class FreeBoardAPI extends SessionCheckCTL {

    private static final Logger logger = LoggerFactory.getLogger(FreeBoardAPI.class); // 로그 설정

    @Autowired
    private FreeBoardSRV freeBoardSRV; // CMPostService 의존성 주입

    @Autowired
    private FreeBoardGoodSRV freeBoardGoodSRV;

    @Autowired
    private UserSRV userSRV; // UserService 의존성 주입

    /* 자유게시판 글 작성 */
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/create") // POST 요청을 "/new" 경로와 매핑
    public ResponseEntity<String> createPost(@ModelAttribute FreeBoardFRM form, HttpSession session) {

        logger.info("Request to create new post: {}", form); // 새 게시글 생성 요청

        Long userId = (Long) session.getAttribute("userId"); // 세션에서 userId 추출
        UserENT loginUser = userSRV.getLoginUserById(userId); // 로그인된 사용자 조회
        if (loginUser == null) {
            logger.error("Logged in user not found."); // 사용자 조회 실패 시 로그 출력
            return ResponseEntity.badRequest().body("User not found"); // 오류 응답 반환
        }

        FreeBoardENT post = form.toEntity(); // CMPostForm을 CMPost 엔티티로 변환
        post.setUser(loginUser); // 로그인한 사용자 설정
        freeBoardSRV.savePost(post); // 게시글 저장
        logger.info("Post saved successfully: {}", post); // 게시글 저장 완료 로그 출력

        return ResponseEntity.ok("Post created successfully"); // 성공 응답 반환
    }

    /* 자유게시판 목록 */
    @GetMapping // GET 요청을 기본 경로와 매핑
    public ResponseEntity<List<FreeBoardGetMethodFRM>> listPosts() {
        logger.info("Requesting post list"); // 게시글 목록 요청
        List<FreeBoardENT> posts = freeBoardSRV.findAllPosts(); // 모든 게시글 조회
        List<FreeBoardGetMethodFRM> freeBoardGetMethodFRMS = posts.stream().map(post -> new FreeBoardGetMethodFRM(
                post.getId(),
                post.getUser(),
                post.getTitle(),
                //post.getLikeCount(),
                post.getRegdate().withNano(0),
                post.getCount()
        )).collect(Collectors.toList()); // 변환된 게시글 리스트로 수집
        logger.info("Post list retrieved successfully"); // 게시글 목록 조회 완료 로그 출력
        return ResponseEntity.ok(freeBoardGetMethodFRMS); // 조회된 게시글 목록 반환
    }

    /* 자유게시판 게시글 열람 */
    @GetMapping("/{id}")
    public ResponseEntity<FreeBoardFRM> getPost(@PathVariable Long id) {
        logger.info("Requesting post detail: Post ID {}", id);  // 로그: 게시글 상세 조회 요청
        FreeBoardENT post = freeBoardSRV.findPostById(id);  // ID로 게시글을 조회
        if (post == null) {
            return ResponseEntity.notFound().build();  // 게시글을 찾지 못한 경우 404 응답 반환
        }

        if (post.isDisable()) {
            logger.warn("Attempted access to deleted post: Post ID {}", id);  // 삭제된 게시글 접근 시 로그 출력

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/posts"));  // /posts 경로로 리다이렉트 설정

            return new ResponseEntity<>(headers, HttpStatus.FOUND);  // 302 Found 상태 코드와 함께 리다이렉트
        }

        // 게시글의 사진을 Base64 인코딩된 문자열로 변환하여 리스트로 수집
        List<String> pictureBase64List = post.getPictures() != null ? post.getPictures().stream()
                .map(picture -> Base64.getEncoder().encodeToString(picture.getPictureData()))  // CMPicture의 byte[] 데이터를 Base64 문자열로 변환
                .collect(Collectors.toList()) : null;

        // 조회된 데이터를 기반으로 CMPostForm 객체 생성
        FreeBoardFRM postForm = new FreeBoardFRM(
                post.getId(),
                post.getUser(),
                post.getTitle(),
                post.getContent(),
                pictureBase64List,  // Base64 인코딩된 사진 리스트를 CMPostForm에 설정
                //post.getLikeCount(),
                post.getRegdate(),
                post.getCount()
        );

        logger.info("Post detail retrieved successfully: Post ID {}", id);  // 로그: 게시글 상세 조회 성공
        return ResponseEntity.ok(postForm);  // CMPostForm 객체를 응답으로 반환
    }

    /* 자유게시판 좋아요 추가 */
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/{id}/like") // POST 요청을 "/{id}/like" 경로와 매핑
    public ResponseEntity<Integer> recommend(@PathVariable Long id, HttpSession httpSession) {
        /*logger.info("Request to increase like count: Post ID {}", id); // 게시글 좋아요 증가 요청
        cmPostService.incrementLikeCount(id); // 게시글의 좋아요 수 증가
        FreeBoardENT post = cmPostService.findPostById(id); // ID로 게시글 조회
        return post != null ? ResponseEntity.ok(post.getLikeCount()) : ResponseEntity.notFound().build(); // 게시글이 존재할 경우 좋아요 수 반환, 그렇지 않으면 404 응답
        */
        Long userId = (Long) httpSession.getAttribute("userId");
        /* 처음 추천하는 경우 반영 & 정상 응답 */
        if (freeBoardGoodSRV.recommend(freeBoardSRV.findPostById(id), userSRV.findUserById(userId)))
            return ResponseEntity.ok(null);
        /* 이미 추천을 했다면 에러코드로 반환 */
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 최신 4개의 게시글을 내림차순으로 반환
    @GetMapping("/recent")
    public List<FreeBoardFRM> getRecentPosts() {
        List<FreeBoardENT> posts = freeBoardSRV.findTop4PostsDesc();  // 최신 게시글 4개를 내림차순으로 조회
        return posts.stream().map(post -> {
            // 각 게시글의 사진을 Base64 인코딩된 문자열로 변환하여 리스트로 수집
            List<String> pictureBase64List = post.getPictures() != null ? post.getPictures().stream()
                    .map(picture -> Base64.getEncoder().encodeToString(picture.getPictureData()))  // CMPicture의 byte[] 데이터를 Base64 문자열로 변환
                    .collect(Collectors.toList()) : null;

            // 조회된 데이터를 기반으로 CMPostForm 객체 생성
            return new FreeBoardFRM(
                    post.getId(),
                    post.getUser(),
                    post.getTitle(),
                    post.getContent(),
                    pictureBase64List,  // Base64 인코딩된 사진 리스트를 CMPostForm에 설정
                    //post.getLikeCount(),
                    post.getRegdate(),
                    post.getCount()
            );
        }).collect(Collectors.toList());  // 변환된 CMPostForm 객체 리스트를 반환
    }

    // 특정 사용자가 작성한 게시글을 내림차순으로 반환
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<FreeBoardFRM>> getPostsByUser(@PathVariable Long userId) {
        logger.info("Requesting posts by user: User ID {}", userId); // 사용자의 게시글 요청
        List<FreeBoardENT> posts = freeBoardSRV.findPostsByUserId(userId); // 사용자의 게시글 조회
        List<FreeBoardFRM> postForms = posts.stream().map(post -> { // 각 게시글을 CMPostForm으로 변환
            List<String> pictureBase64List = post.getPictures() != null ? post.getPictures().stream()
                    .map(picture -> Base64.getEncoder().encodeToString(picture.getPictureData()))  // CMPicture의 byte[] 데이터를 Base64 문자열로 변환
                    .collect(Collectors.toList()) : null;
            return new FreeBoardFRM(
                    post.getId(),
                    post.getUser(),
                    post.getTitle(),
                    post.getContent(),
                    pictureBase64List,
                    //post.getLikeCount(),
                    post.getRegdate(),
                    post.getCount()
            );
        }).collect(Collectors.toList()); // 변환된 게시글 리스트로 수집
        logger.info("Posts by user retrieved successfully: User ID {}", userId); // 사용자의 게시글 조회 완료
        return ResponseEntity.ok(postForms); // 조회된 게시글 목록 반환
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PutMapping("/{id}/update")
    public ResponseEntity<String> updatePost(
            @PathVariable Long id,
            @ModelAttribute FreeBoardFRM form,  // @ModelAttribute로 폼 데이터 수신
            HttpSession session) {

        logger.info("Request to update post ID {}: {}", id, form);  // 게시글 수정 요청 로그

        Long userId = (Long) session.getAttribute("userId");
        UserENT loginUser = userSRV.getLoginUserById(userId);
        if (loginUser == null) {
            logger.error("Logged in user not found.");
            return ResponseEntity.badRequest().body("User not found");
        }

        FreeBoardENT existingPost = freeBoardSRV.findPostById(id);
        if (existingPost == null || !existingPost.getUser().getId().equals(loginUser.getId())) {
            logger.error("Post not found or unauthorized update attempt.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Post not found or unauthorized");
        }

        // 게시글 제목과 내용을 업데이트
        existingPost.setTitle(form.getTitle());
        existingPost.setContent(form.getContent());

        // 사진이 제공된 경우에만 사진을 업데이트
        if (form.getPictures() != null && !form.getPictures().isEmpty()) {
            List<byte[]> pictureBytesList = form.getPictures().stream()
                    .map(picture -> {
                        try {
                            return picture.getBytes();
                        } catch (IOException e) {
                            logger.error("Error reading picture bytes: {}", e.getMessage());
                            return null;
                        }
                    })
                    .collect(Collectors.toList());

            // 새로운 사진 데이터로 사진 목록 업데이트
            List<FreeBoardPictureENT> newPictures = pictureBytesList.stream()
                    .map(bytes -> FreeBoardPictureENT.builder()
                            .cmPost(existingPost)
                            .pictureData(bytes)
                            .build())
                    .collect(Collectors.toList());

            existingPost.getPictures().clear();  // 기존 사진 제거
            existingPost.getPictures().addAll(newPictures);  // 새 사진 추가
        }

        freeBoardSRV.savePost(existingPost);  // 수정된 게시글 저장
        logger.info("Post updated successfully: {}", existingPost);  // 게시글 수정 완료 로그
        return ResponseEntity.ok("Post updated successfully");
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PutMapping("/{id}/delete")
    public ResponseEntity<String> deletePost(@PathVariable Long id, HttpSession session) {

        logger.info("Request to delete post ID {}", id);  // 게시글 삭제 요청 로그

        Long userId = (Long) session.getAttribute("userId");
        UserENT loginUser = userSRV.getLoginUserById(userId);
        if (loginUser == null) {
            logger.error("Logged in user not found.");
            return ResponseEntity.badRequest().body("User not found");
        }

        FreeBoardENT existingPost = freeBoardSRV.findPostById(id);
        if (existingPost == null || !existingPost.getUser().getId().equals(loginUser.getId())) {
            logger.error("Post not found or unauthorized delete attempt.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Post not found or unauthorized");
        }

        existingPost.setDisable(true);  // 게시글을 삭제로 설정
        freeBoardSRV.savePost(existingPost);  // 수정된 게시글 저장

        logger.info("Post deleted successfully: {}", existingPost);  // 게시글 삭제 완료 로그
        return ResponseEntity.ok("Post deleted successfully");
    }
}
