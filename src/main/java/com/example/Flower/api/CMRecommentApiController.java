package com.example.Flower.api;

import com.example.Flower.controller.SessionCheckController;
import com.example.Flower.dto.CMRecommentForm;
import com.example.Flower.entity.CMPost;
import com.example.Flower.entity.CMRecomment;
import com.example.Flower.entity.CMComment;
import com.example.Flower.entity.User;
import com.example.Flower.service.CMRecommentService;
import com.example.Flower.service.CMCommentService;
import com.example.Flower.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/recomments")
public class CMRecommentApiController extends SessionCheckController {

    private static final Logger logger = LoggerFactory.getLogger(CMRecommentApiController.class); // 로그 설정

    @Autowired
    private CMRecommentService cmRecommentService; // CMRecommentService 의존성 주입

    @Autowired
    private CMCommentService cmCommentService; // CMCommentService 의존성 주입

    @Autowired
    private UserService userService; // UserService 의존성 주입

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/new") // POST 요청을 "/new" 경로와 매핑
    public ResponseEntity<String> createRecomment(@ModelAttribute CMRecommentForm form, HttpSession session) {
        logger.info("Request to create new recomment: {}", form); // 새 대댓글 생성 요청

        Long userId = (Long) session.getAttribute("userId"); // 세션에서 userId 추출
        User loginUser = userService.getLoginUserById(userId); // 로그인된 사용자 조회
        if (loginUser == null) {
            logger.error("Logged in user not found."); // 사용자 조회 실패 시 로그 출력
            return ResponseEntity.badRequest().body("User not found"); // 오류 응답 반환
        }

        logger.info("Logged in user: {}", loginUser.getNickname()); // 로그인된 사용자 정보 로그 출력

        CMComment comment = cmCommentService.findCommentById(form.getCmCommentId().getId()); // 댓글 ID로 댓글 조회
        CMPost post = comment.getCmPost(); // 댓글에서 게시글 가져오기

        if (comment != null && post != null) {
            CMRecomment recomment = new CMRecomment(); // 새로운 대댓글 생성
            recomment.setCmComment(comment); // 대댓글에 댓글 설정
            recomment.setCmPost(post); // 대댓글에 게시글 설정
            recomment.setContent(form.getContent()); // 대댓글 내용 설정
            recomment.setLikeCount(0); // 기본 좋아요 수 0으로 설정
            recomment.setUser(loginUser); // 대댓글 작성자 설정
            cmRecommentService.saveRecomment(recomment); // 대댓글 저장

            logger.info("Recomment saved successfully: {}", recomment); // 대댓글 저장 완료 로그 출력
            return ResponseEntity.ok("Recomment created successfully"); // 성공 응답 반환
        }

        return ResponseEntity.badRequest().body("Comment or Post not found"); // 댓글이나 게시글을 찾을 수 없는 경우 오류 응답 반환
    }


    @GetMapping("/comment/{commentId}") // GET 요청을 "/comment/{commentId}" 경로와 매핑
    public ResponseEntity<List<CMRecomment>> listRecomments(@PathVariable Long commentId) {
        logger.info("Requesting recomment list: Comment ID {}", commentId); // 대댓글 목록 요청
        List<CMRecomment> recomments = cmRecommentService.findRecommentsByCommentId(commentId); // 댓글의 모든 대댓글 조회
        logger.info("Recomment list retrieved successfully: Comment ID {}", commentId); // 대댓글 목록 조회 완료
        return ResponseEntity.ok(recomments); // 조회된 대댓글 목록 반환
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/{id}/like") // POST 요청을 "/{id}/like" 경로와 매핑
    public ResponseEntity<Integer> likeRecomment(@PathVariable Long id) {
        logger.info("Request to increase like count: Recomment ID {}", id); // 대댓글 좋아요 증가 요청
        cmRecommentService.incrementLikeCount(id); // 대댓글의 좋아요 수 증가
        CMRecomment recomment = cmRecommentService.findRecommentById(id); // ID로 대댓글 조회
        return recomment != null ? ResponseEntity.ok(recomment.getLikeCount()) : ResponseEntity.notFound().build(); // 대댓글이 존재할 경우 좋아요 수 반환, 그렇지 않으면 404 응답
    }
}
