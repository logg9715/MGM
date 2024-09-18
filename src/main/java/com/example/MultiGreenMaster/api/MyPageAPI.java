package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.dto.FreeBoardCommentFRM;
import com.example.MultiGreenMaster.dto.FreeBoardFRM;
import com.example.MultiGreenMaster.entity.FreeBoardCommentENT;
import com.example.MultiGreenMaster.entity.FreeBoardENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.FreeBoardCommentSRV;
import com.example.MultiGreenMaster.service.FreeBoardSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mypage")
public class MyPageAPI {
    @Autowired
    FreeBoardSRV freeBoardSRV;

    @Autowired
    UserSRV userSRV;

    /* 유저 기본 정보 반환 */
    // 비밀번호, 신상 등의 개인정보는 삭제함
    // 존재하지 않는 계정일 경우 badrequest 반환

    @GetMapping("/{userId}/userinfo")
    public ResponseEntity<UserENT> getUserInfo(@PathVariable Long userId) {
        UserENT target = userSRV.findUserById(userId);

        if (target == null)
            return ResponseEntity.badRequest().build();

        target.guestBookPatch();
        return ResponseEntity.ok(target);
    }

    /* 특정 사용자가 작성한 최신 게시글 3개 반환 */
    // 필요 없는 정보(작성자, 사진)은 보내지 않음

    @GetMapping("/{userId}/recentfreeboard")
    public ResponseEntity<List<FreeBoardFRM>> getRecentFreeBroad(@PathVariable Long userId) {
        List<FreeBoardENT> posts = freeBoardSRV.findLast3FreeboardByUserId(userId); // 사용자의 게시글 조회
        List<FreeBoardFRM> postForms = posts.stream().map(post -> { // 각 게시글을 CMPostForm으로 변환
            return new FreeBoardFRM(
                    post.getId(),
                    null,
                    post.getTitle(),
                    post.getContent(),
                    null,
                    post.getRegdate(),
                    post.getCount()
            );
        }).collect(Collectors.toList()); // 변환된 게시글 리스트로 수집
        return ResponseEntity.ok(postForms); // 조회된 게시글 목록 반환
    }

    /* 특정 사용자의 최신 댓글 3개 반환 */

    @GetMapping("/{userId}/recentfreeboardcomment")
    public ResponseEntity<List<FreeBoardCommentFRM>> getRecentFreeBoardComment(@PathVariable Long userId) {
        List<FreeBoardCommentFRM> freeBoardCommentFRMS = userSRV.getUserCommentsAndRecommentsLast3(userId);
        return ResponseEntity.ok(freeBoardCommentFRMS);
    }


}
