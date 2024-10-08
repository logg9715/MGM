package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.Util.AccessAuthority;
import com.example.MultiGreenMaster.dto.FreeBoardCommentFRM;
import com.example.MultiGreenMaster.dto.FreeBoardCommentFRM_V2;
import com.example.MultiGreenMaster.dto.FreeBoardFRM;
import com.example.MultiGreenMaster.dto.UserFRM;
import com.example.MultiGreenMaster.entity.FreeBoardENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.FreeBoardSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                    post.getRegdate().withNano(0),
                    post.getCount()
            );
        }).collect(Collectors.toList()); // 변환된 게시글 리스트로 수집
        return ResponseEntity.ok(postForms); // 조회된 게시글 목록 반환
    }

    /* 특정 사용자의 최신 댓글 3개 반환 */
    @GetMapping("/{userId}/recentfreeboardcomment")
    public ResponseEntity<List<FreeBoardCommentFRM_V2>> getRecentFreeBoardComment(@PathVariable Long userId) {
        List<FreeBoardCommentFRM_V2> freeBoardCommentFRMS = userSRV.getUserCommentsAndRecommentsLast3(userId);
        return ResponseEntity.ok(freeBoardCommentFRMS);
    }

    // =============================== 잔디 정보 불러오기 ===========================================

    @GetMapping("/{userId}/grassdata")
    public ResponseEntity<?> getGrassData(@PathVariable Long userId) {
        return ResponseEntity.ok(userSRV.getGrass(userId));
    }


    // =============================== 마이페이지에서 계정 정보 수정 ===========================================

    /* 개인정보 수정용 form 받아오는 api */
    // 세션 정보를 읽어 자동으로 본인의 정보를 가져온다.
    @GetMapping("/editinfo/getuserform")
    public ResponseEntity<UserENT> editInfo_getUserForm(HttpSession session) {
        // 본인의 정보를 자동으로 가져온다.
        UserENT target = this.userSRV.findUserById((Long)session.getAttribute("userId"));
        return ResponseEntity.ok(target);
    }
    /* form을 전달받으면 업데이트하는 api */
    @PostMapping("/editinfo/update")
    public ResponseEntity<UserENT> editInfo_update(@RequestBody UserFRM form, HttpSession session) {
        // 본인의 경우만 허용, 아니면 badRequest 반환
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userSRV);
        if (accessAuthority.forOwner(form.getId()).isOk())
        {
            UserENT updatedUser = userSRV.updateUser(form);
            return (updatedUser == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(updatedUser);
        }
        else
            return ResponseEntity.badRequest().build();
    }

}
