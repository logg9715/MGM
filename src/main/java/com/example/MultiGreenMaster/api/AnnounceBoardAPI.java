package com.example.MultiGreenMaster.api;
// 공지사항 api 컨트롤러
import com.example.MultiGreenMaster.controller.SessionCheckCTL;
import com.example.MultiGreenMaster.dto.AnnounceBoardFRM;
import com.example.MultiGreenMaster.entity.AnnounceBoardENT;
import com.example.MultiGreenMaster.repository.AnnounceBoardREP;
import com.example.MultiGreenMaster.repository.UserREP;
import com.example.MultiGreenMaster.service.AnnounceBoardSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/announces")
public class AnnounceBoardAPI extends SessionCheckCTL {

    @Autowired
    private AnnounceBoardSRV announceBoardSRV;

    /* 공지사항 목록 */
    @GetMapping
    public ResponseEntity<List<AnnounceBoardENT>> index() {
        return ResponseEntity.ok(announceBoardSRV.getAllEnableAnnounces());
    }

    /* 새 공지사항 제출 */
    @PostMapping("/create")
    public ResponseEntity<AnnounceBoardENT> createAnnounce(@RequestBody AnnounceBoardFRM form, Model model) {
        return ResponseEntity.ok(announceBoardSRV.saveNewAnnounce(form));
    }

    /* 공지사항 열람 */
    @GetMapping("/{id}")
    public ResponseEntity<AnnounceBoardENT> show(@PathVariable("id") Long id) {
        AnnounceBoardENT announceEntity = announceBoardSRV.findAnnounceById(id);
        if(announceEntity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(announceEntity);
    }

    /* 최근 4개 공지사항 리스트 전송 */
    @GetMapping("/recent")
    public ResponseEntity<List<AnnounceBoardENT>> recent4AnnounceBoard() {
        List<AnnounceBoardENT> recentAnnounces = announceBoardSRV.getRecentAnnounces();
        return ResponseEntity.ok(recentAnnounces);
    }

    /* 공지사항 수정 제출 */
    @PatchMapping("/{id}/edit")
    public ResponseEntity<AnnounceBoardENT> edit(@PathVariable Long id, @RequestBody AnnounceBoardFRM form) {
        AnnounceBoardENT updatedAnnounce = announceBoardSRV.updateAnnounce(id, form);
        if(updatedAnnounce == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(updatedAnnounce);
    }

    /* 공지사항 삭제(비활성화) */
    @DeleteMapping("/{id}")
    public ResponseEntity<AnnounceBoardENT> delete(@PathVariable Long id, RedirectAttributes rttr) {
        AnnounceBoardENT deletedAnnounce = announceBoardSRV.deleteAnnounce(id);
        if (deletedAnnounce != null) rttr.addFlashAttribute("msg", "삭제됐습니다!");
        return ResponseEntity.ok(deletedAnnounce);
    }
}
