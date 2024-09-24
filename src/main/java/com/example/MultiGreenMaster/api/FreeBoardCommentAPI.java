package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.controller.SessionCheckCTL;
import com.example.MultiGreenMaster.dto.FreeBoardCommentFRM;
import com.example.MultiGreenMaster.entity.FreeBoardCommentENT;
import com.example.MultiGreenMaster.entity.FreeBoardENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.FreeBoardCommentSRV;
import com.example.MultiGreenMaster.service.FreeBoardSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/freeboardcomment") // "/api/comments" 경로와 매핑
public class FreeBoardCommentAPI extends SessionCheckCTL {

    private static final Logger logger = LoggerFactory.getLogger(FreeBoardCommentAPI.class); // 로그 설정

    @Autowired
    private FreeBoardCommentSRV freeBoardCommentSRV; // CMCommentService 의존성 주입

    @Autowired
    private FreeBoardSRV cmPostService; // CMPostService 의존성 주입

    @Autowired
    private UserSRV userService; // UserService 의존성 주입


    @PostMapping("/create") // POST 요청을 "/new" 경로와 매핑
    public ResponseEntity<String> createComment(@ModelAttribute FreeBoardCommentFRM form, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId"); // 세션에서 userId 추출
        UserENT loginUser = userService.getLoginUserById(userId); // 로그인된 사용자 조회
        if (loginUser == null)
            return ResponseEntity.badRequest().body("User not found"); // 오류 응답 반환

        FreeBoardENT post = cmPostService.findPostById(form.getCmPostId().getId()); // 게시글 ID로 게시글 조회

        if (post != null) {
            FreeBoardCommentENT comment = form.toEntity(); // 폼 데이터를 통해 엔티티 생성
            comment.setCmPost(post); // 댓글에 게시글 설정
            comment.setUser(loginUser); // 댓글에 사용자 설정
            comment.setTimeNow();
            freeBoardCommentSRV.saveComment(comment); // 댓글 저장
            return ResponseEntity.ok("Comment created successfully"); // 성공 응답 반환
        }

        return ResponseEntity.badRequest().body("Post not found"); // 게시글을 찾을 수 없는 경우 오류 응답 반환
    }

    @GetMapping("/{postId}") // GET 요청을 "/post/{postId}" 경로와 매핑
    public ResponseEntity<List<FreeBoardCommentENT>> listComments(@PathVariable Long postId) {
        logger.info("Requesting comment list: Post ID {}", postId); // 댓글 목록 요청
        List<FreeBoardCommentENT> comments = freeBoardCommentSRV.findCommentsByPostId(postId); // 게시글의 모든 댓글 조회

        // 비활성화된 댓글의 내용을 "삭제된 댓글입니다"로 수정
        comments.forEach(comment -> {
            if (comment.isDisable()) {
                comment.setContent("삭제된 댓글입니다");
            }
        });

        logger.info("Comment list retrieved successfully: Post ID {}", postId); // 댓글 목록 조회 완료
        return ResponseEntity.ok(comments); // 조회된 댓글 목록 반환
    }

    @PostMapping("/{id}/create")
    public ResponseEntity<String> createReply(@PathVariable Long id, @ModelAttribute FreeBoardCommentFRM form, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            UserENT loginUser = userService.getLoginUserById(userId);
            if (loginUser == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // cmPostId가 null인지 확인하고 방어 처리
            if (form.getCmPostId() == null || form.getCmPostId().getId() == null) {
                return ResponseEntity.badRequest().body("Post ID is required");
            }

            FreeBoardENT post = cmPostService.findPostById(form.getCmPostId().getId());
            FreeBoardCommentENT parentComment = freeBoardCommentSRV.findCommentById(id);

            if (post != null && parentComment != null) {
                // 새로운 CMComment 객체 생성 (대댓글)
                FreeBoardCommentENT newComment = new FreeBoardCommentENT();
                newComment.setCmPost(post); // 게시글 설정
                newComment.setUser(loginUser); // 작성자 설정
                newComment.setContent(form.getContent()); // 내용 설정
                newComment.setParentComment(parentComment); // 부모 댓글 설정
                newComment.setTimeNow();

                // 대댓글을 저장
                freeBoardCommentSRV.saveComment(newComment);
                return ResponseEntity.ok("Reply created successfully");
            }

            return ResponseEntity.badRequest().body("Post or Parent Comment not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request.");
        }
    }


    @PutMapping("/{id}/update")
    public ResponseEntity<String> updateComment(
            @PathVariable Long id,
            @RequestBody String content,  // 클라이언트로부터 JSON 형식의 content를 직접 받음
            HttpSession session) {

        logger.info("Update comment request received for comment ID: {}", id);
        logger.info("Content to update: {}", content);

        // 세션에서 사용자 ID를 가져옴
        Long userId = (Long) session.getAttribute("userId");
        FreeBoardCommentENT existingComment = freeBoardCommentSRV.findCommentById(id);

        // 댓글이 존재하지 않거나, 현재 사용자가 댓글 작성자가 아닌 경우
        if (existingComment == null || !existingComment.getUser().getId().equals(userId)) {
            logger.error("Comment not found or unauthorized: Comment ID: {}, User ID: {}", id, userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Comment not found or unauthorized");
        }

        // 댓글 내용 수정
        freeBoardCommentSRV.updateComment(id, content);

        // 성공 메시지 반환
        logger.info("Comment updated successfully for comment ID: {}", id);
        return ResponseEntity.ok("Comment updated successfully");
    }


    @PutMapping("/{id}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, HttpSession session) {
        logger.info("Request to delete comment ID: {}", id);

        Long userId = (Long) session.getAttribute("userId");
        FreeBoardCommentENT existingComment = freeBoardCommentSRV.findCommentById(id);

        // 댓글이 존재하지 않거나, 현재 사용자가 댓글 작성자가 아닌 경우
        if (existingComment == null || !existingComment.getUser().getId().equals(userId)) {
            logger.error("Comment not found or unauthorized: Comment ID: {}, User ID: {}", id, userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Comment not found or unauthorized");
        }

        // 댓글 삭제
        existingComment.setDisable(true);
        freeBoardCommentSRV.saveComment(existingComment);

        logger.info("Comment deleted successfully: Comment ID: {}", id);
        return ResponseEntity.ok("Comment deleted successfully");
    }

}
