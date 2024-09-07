package com.example.MultiGreenMaster.controller;

import com.example.MultiGreenMaster.dto.FreeBoardCommentFRM;
import com.example.MultiGreenMaster.entity.FreeBoard_CommentENT;
import com.example.MultiGreenMaster.entity.CMPostENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.CMCommentSRV;
import com.example.MultiGreenMaster.service.CMPostSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comments") // "/comments" 경로와 매핑
public class CMCommentCtl {

    private static final Logger logger = LoggerFactory.getLogger(CMCommentCtl.class); // 로그 설정

    @Autowired
    private CMCommentSRV cmCommentService;

    @Autowired
    private CMPostSRV cmPostService;

    @Autowired
    private UserSRV userService;

    @PostMapping("/new")
    public String createComment(FreeBoardCommentFRM form, HttpServletRequest request) {
        logger.info("Request to create new comment: {}", form);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            logger.error("No user is logged in.");
            return "redirect:/session-login/login";
        }

        Long userId = (Long) session.getAttribute("userId");
        UserENT loginUser = userService.getLoginUserById(userId);
        if (loginUser == null) {
            logger.error("Logged in user not found.");
            return "redirect:/session-login/login";
        }

        CMPostENT post = cmPostService.findPostById(form.getCmPostId().getId());
        if (post != null) {
            FreeBoard_CommentENT comment = new FreeBoard_CommentENT();
            comment.setCmPost(post);
            comment.setContent(form.getContent());
            comment.setLikeCount(0);
            comment.setUser(loginUser); // Set the logged-in user to the comment
            cmCommentService.saveComment(comment);
            logger.info("Comment saved successfully: {}", comment);
        }

        return "redirect:/posts/" + form.getCmPostId().getId();
    }

    @GetMapping("/post/{postId}") // GET 요청을 "/post/{postId}" 경로와 매핑
    public String listComments(@PathVariable Long postId, Model model) {
        logger.info("Requesting comment list: Post ID {}", postId); // 댓글 목록 요청
        List<FreeBoard_CommentENT> comments = cmCommentService.findCommentsByPostId(postId); // 게시글의 모든 댓글 조회
        model.addAttribute("comments", comments); // 댓글 리스트를 모델에 추가
        logger.info("Comment list retrieved successfully: Post ID {}", postId); // 댓글 목록 조회 완료
        return "commentList"; // "commentList" 뷰를 반환
    }

    @PostMapping("/{id}/like")
    @ResponseBody
    public int likeComment(@PathVariable Long id) {
        logger.info("Request to increase like count: Comment ID {}", id); // 댓글 좋아요 증가 요청
        FreeBoard_CommentENT comment = cmCommentService.findCommentById(id);
        cmCommentService.incrementLikeCount(id);
        return comment != null ? comment.getLikeCount() : 0;
    }
}
