package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.controller.SessionCheckCTL;
import com.example.MultiGreenMaster.dto.FreeBoardCommentFRM;
import com.example.MultiGreenMaster.entity.FreeBoard_CommentENT;
import com.example.MultiGreenMaster.entity.CMPostENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.CMCommentSRV;
import com.example.MultiGreenMaster.service.CMPostSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments") // "/api/comments" 경로와 매핑
public class CMCommentAPI extends SessionCheckCTL {

    private static final Logger logger = LoggerFactory.getLogger(CMCommentAPI.class); // 로그 설정

    @Autowired
    private CMCommentSRV cmCommentService; // CMCommentService 의존성 주입

    @Autowired
    private CMPostSRV cmPostService; // CMPostService 의존성 주입

    @Autowired
    private UserSRV userService; // UserService 의존성 주입

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/new") // POST 요청을 "/new" 경로와 매핑
    public ResponseEntity<String> createComment(@ModelAttribute FreeBoardCommentFRM form, HttpSession session) {
        logger.info("Request to create new comment: {}", form); // 새 댓글 생성 요청

        Long userId = (Long) session.getAttribute("userId"); // 세션에서 userId 추출
        UserENT loginUser = userService.getLoginUserById(userId); // 로그인된 사용자 조회
        if (loginUser == null) {
            logger.error("Logged in user not found."); // 사용자 조회 실패 시 로그 출력
            return ResponseEntity.badRequest().body("User not found"); // 오류 응답 반환
        }

        CMPostENT post = cmPostService.findPostById(form.getCmPostId().getId()); // 게시글 ID로 게시글 조회

        if (post != null) {
            FreeBoard_CommentENT comment = new FreeBoard_CommentENT(); // 새로운 댓글 생성
            comment.setCmPost(post); // 댓글에 게시글 설정
            comment.setContent(form.getContent()); // 댓글 내용 설정
            comment.setLikeCount(0); // 기본 좋아요 수 0으로 설정
            comment.setUser(loginUser); // Set the logged-in user to the comment
            cmCommentService.saveComment(comment); // 댓글 저장
            logger.info("Comment saved successfully: {}", comment); // 댓글 저장 완료
            return ResponseEntity.ok("Comment created successfully"); // 성공 응답 반환
        }

        return ResponseEntity.badRequest().body("Post not found"); // 게시글을 찾을 수 없는 경우 오류 응답 반환
    }

    @GetMapping("/post/{postId}") // GET 요청을 "/post/{postId}" 경로와 매핑
    public ResponseEntity<List<FreeBoard_CommentENT>> listComments(@PathVariable Long postId) {
        logger.info("Requesting comment list: Post ID {}", postId); // 댓글 목록 요청
        List<FreeBoard_CommentENT> comments = cmCommentService.findCommentsByPostId(postId); // 게시글의 모든 댓글 조회
        logger.info("Comment list retrieved successfully: Post ID {}", postId); // 댓글 목록 조회 완료
        return ResponseEntity.ok(comments); // 조회된 댓글 목록 반환
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/{id}/like")
    public ResponseEntity<Integer> likeComment(@PathVariable Long id) {
        logger.info("Request to increase like count: Comment ID {}", id); // 댓글 좋아요 증가 요청
        cmCommentService.incrementLikeCount(id); // 댓글의 좋아요 수 증가
        FreeBoard_CommentENT comment = cmCommentService.findCommentById(id); // ID로 댓글 조회
        return comment != null ? ResponseEntity.ok(comment.getLikeCount()) : ResponseEntity.notFound().build(); // 댓글이 존재할 경우 좋아요 수 반환, 그렇지 않으면 404 응답
    }

}
