package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.Util.AccessAuthority;
import com.example.MultiGreenMaster.controller.SessionCheckCTL;
import com.example.MultiGreenMaster.dto.DiaryBoardFRM;
import com.example.MultiGreenMaster.entity.DiaryBoardENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.exeption.DiaryDeleteExcption;
import com.example.MultiGreenMaster.exeption.DiaryNotFoundException;
import com.example.MultiGreenMaster.service.DiaryBoardSRV;
import com.example.MultiGreenMaster.service.FriendSRV;
import com.example.MultiGreenMaster.service.UserSRV;
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
@RequestMapping("/api/diaries")
public class DiaryBoardAPI extends SessionCheckCTL {

    private static final Logger logger = LoggerFactory.getLogger(DiaryBoardAPI.class); // 로그 설정

    @Autowired
    private DiaryBoardSRV diaryService; // DiaryService 의존성 주입

    @Autowired
    private UserSRV userService; // UserService 의존성 주입

    @Autowired
    private FriendSRV friendSRV;

    @PostMapping("/create")
    public ResponseEntity<String> createDiary(@ModelAttribute DiaryBoardFRM form, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        UserENT loginUser = userService.getLoginUserById(userId);
        if (loginUser == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // 엔티티로 변환 및 사용자 설정
        DiaryBoardENT diary = form.toEntity();
        diary.setUser(loginUser);
        diary.setTimeNow();

        diaryService.saveDiary(diary);
        return ResponseEntity.ok("Diary created successfully");
    }


    /* 일기장 목록 가져오기 */
    @GetMapping("/{userId}")
    public ResponseEntity<List<DiaryBoardFRM>> listDiaries(@PathVariable Long userId, HttpSession session) {
        // ============================================ 접근제한자 ==================================================
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userService, this.friendSRV);
        // case 1. 접근제한 = 본인
        Integer diaryLevel = userService.getDiaryLevel(userId);
        if (diaryLevel == null || diaryLevel == 0)
        {
            System.out.println("case 1");
            if (!accessAuthority.forOwner(userId).isOk())
                return ResponseEntity.badRequest().build();
        }
        // case 2. 접근제한 = 본인&친구
        else if (diaryLevel == 1)
        {
            System.out.println("case 2");
            if (!accessAuthority.forOwner(userId).forFriend(userId).isOk())
                return ResponseEntity.badRequest().build();
        }
        // case 3. 접근제한 = 전체공개
        else if (diaryLevel == 2) {
            /* 통과 */
            System.out.println("case 3");
        }
        else
            return ResponseEntity.badRequest().build();
        // =================================================================================================

        // 일기장 리스트 조회
        List<DiaryBoardENT> diaries = diaryService.findDiariesForUser(userId);
        List<DiaryBoardFRM> diaryForms = diaries.stream().map(diary -> {
            List<String> pictureBase64List = diary.getPictures() != null ? diary.getPictures().stream()
                    .map(picture -> Base64.getEncoder().encodeToString(picture))
                    .collect(Collectors.toList()) : null;

            return new DiaryBoardFRM(
                    diary.getId(),
                    diary.getUser(),
                    diary.getTitle(),
                    diary.getContent(),
                    null,
                    diary.getRegdate().withNano(0),
                    diary.isDisable()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(diaryForms); // 일기장 목록 반환
    }

    /* 특정 일기장 조회 */
    @GetMapping("/{userId}/{id}")
    public ResponseEntity<?> getDiary(@PathVariable Long userId, @PathVariable Long id, HttpSession session) {
        logger.info("Requesting diary detail: Diary ID {}", id);  // 로그: 다이어리 상세 조회 요청

        // ============================================ 접근제한자 ==================================================
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userService, this.friendSRV);
        // case 1. 접근제한 = 본인
        Integer diaryLevel = userService.getDiaryLevel(userId);
        if (diaryLevel == null || diaryLevel == 0)
        {
            if (!accessAuthority.forOwner(userId).isOk())
                return ResponseEntity.badRequest().build();
        }
        // case 2. 접근제한 = 본인&친구
        else if (diaryLevel == 1)
        {
            if (!accessAuthority.forOwner(userId).forFriend(userId).isOk())
                return ResponseEntity.badRequest().build();
        }
        // case 3. 접근제한 = 전체공개
        else if (diaryLevel == 2) {
            /* 통과 */
        }
        else
            return ResponseEntity.badRequest().build();
        // ========================================================================================================

        // 다이어리 조회
        DiaryBoardENT diary = diaryService.findDiaryById(id);
        if (diary == null) {
            logger.error("Diary not found: Diary ID {}", id);  // 존재하지 않는 게시글에 접근 시 오류 로그
            return ResponseEntity.status(404).body("Diary not found");  // 404 오류 반환
        }

        if (diary.isDisable()) {
            logger.error("Attempt to access a deleted diary. Diary ID {}", id);  // 삭제된 게시글에 접근 시 오류 로그
            return ResponseEntity.status(410).body("This diary has been deleted.");  // 410 Gone 오류 반환
        }

        List<String> pictureBase64List = diary.getPictures() != null ? diary.getPictures().stream()
                .map(picture -> Base64.getEncoder().encodeToString(picture))  // byte[] 데이터를 Base64 문자열로 변환
                .collect(Collectors.toList()) : null;

        DiaryBoardFRM diaryForm = new DiaryBoardFRM(
                diary.getId(),
                diary.getUser(),
                diary.getTitle(),
                diary.getContent(),
                pictureBase64List,
                diary.getRegdate().withNano(0),
                diary.isDisable()
        );

        logger.info("Diary detail retrieved successfully: Diary ID {}", id);  // 로그: 다이어리 상세 조회 성공
        return ResponseEntity.ok(diaryForm);  // DiaryForm 객체를 응답으로 반환
    }



    @PutMapping("/{userId}/{id}/update")
    public ResponseEntity<String> updateDiary(@PathVariable Long userId, @PathVariable Long id, @ModelAttribute DiaryBoardFRM form, HttpSession session) {
        // 세션에서 로그인된 사용자 확인
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userService);
        if (!accessAuthority.forOwner(userId).isOk()) {
            return ResponseEntity.badRequest().body("Login User Error");
        }

        // 기존 다이어리 조회
        DiaryBoardENT existingDiary = diaryService.findDiaryById(id);
        if (existingDiary == null || !existingDiary.getUser().getId().equals(userId)) {
            logger.error("Diary not found or unauthorized update attempt.");
            return ResponseEntity.status(403).body("Diary not found or unauthorized");
        }

        // 다이어리 제목 및 내용 업데이트
        existingDiary.setTitle(form.getTitle());
        existingDiary.setContent(form.getContent());

        // 사진이 새로 업로드되었을 때만 처리
        if (form.getPictures() != null && !form.getPictures().isEmpty()) {
            // MultipartFile -> byte[] 변환
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

            // 기존 사진 제거 및 새 사진 추가
            existingDiary.getPictures().clear();  // 기존 사진 목록 제거
            existingDiary.getPictures().addAll(pictureBytesList);  // 새로 업로드된 사진 목록 추가
        }

        // 수정된 다이어리 저장
        diaryService.saveDiary(existingDiary);
        logger.info("Diary updated successfully: {}", existingDiary);  // 로그 출력
        return ResponseEntity.ok("Diary updated successfully");
    }


    @PutMapping("/{userId}/{id}/delete")
    public ResponseEntity<String> deleteDiary(@PathVariable Long userId, @PathVariable Long id, HttpSession session) {
        AccessAuthority accessAuthority = new AccessAuthority(session, this.userService);
        if (!accessAuthority.forOwner(userId).isOk())
            return ResponseEntity.badRequest().body("Login User error");

        DiaryBoardENT existingDiary = diaryService.findDiaryById(id);
        if (existingDiary == null || !existingDiary.getUser().getId().equals(userId)) {
            logger.error("Diary not found or unauthorized delete attempt.");
            return ResponseEntity.status(403).body("Diary not found or unauthorized");
        }

        existingDiary.setDisable(true);  // 다이어리를 삭제로 설정
        diaryService.saveDiary(existingDiary);  // 수정된 다이어리 저장

        logger.info("Diary deleted successfully: {}", existingDiary);  // 다이어리 삭제 완료 로그
        return ResponseEntity.ok("Diary deleted successfully");
    }
}
