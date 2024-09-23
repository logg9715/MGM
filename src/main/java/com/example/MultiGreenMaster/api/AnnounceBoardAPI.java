package com.example.MultiGreenMaster.api;
// 공지사항 api 컨트롤러
import com.example.MultiGreenMaster.controller.SessionCheckCTL;
import com.example.MultiGreenMaster.dto.AnnounceBoardFRM;
import com.example.MultiGreenMaster.entity.AnnounceBoardENT;
import com.example.MultiGreenMaster.repository.AnnounceBoardREP;
import com.example.MultiGreenMaster.repository.UserREP;
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
    private AnnounceBoardREP announceRepository;
    @Autowired
    private UserREP userRepository;
    @Autowired
    private UserSRV userService;

    /* 공지사항 목록 */
    @GetMapping
    public ResponseEntity<List<AnnounceBoardENT>> index() {
        List<AnnounceBoardENT> announceEntityList = (List<AnnounceBoardENT>) announceRepository.findByDisableFalse();
        return ResponseEntity.ok(announceEntityList);
    }

    /* 새 공지사항 제출 */
    @PostMapping("/create")
    public ResponseEntity<AnnounceBoardENT> createAnnounce(@RequestBody AnnounceBoardFRM form, Model model) {  //DTO AnnounceForm
        AnnounceBoardENT announce = form.toEntity();
        AnnounceBoardENT saved = announceRepository.save(announce);
        return ResponseEntity.ok(saved);
    }

    /* 공지사항 열람 */
    @GetMapping("/{id}")
    public ResponseEntity<AnnounceBoardENT> show(@PathVariable("id") Long id) {
        AnnounceBoardENT announceEntity = announceRepository.findById(id).orElse(null);
        if(announceEntity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(announceEntity);
    }

    /* 공지사항 수정 제출 */
    @PatchMapping("/{id}/edit")
    public ResponseEntity<AnnounceBoardENT> edit(@PathVariable Long id, @RequestBody AnnounceBoardFRM form) {
        AnnounceBoardENT newData = form.toEntity();
        System.out.println("@@@@ : 폼 id " + newData.getContent());
        AnnounceBoardENT target = announceRepository.findById(id).orElse(null);

        if(target == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();    // 코드가 없거나 존재하지 않는 글일 경우 튕겨냄
        else
            target.patch(newData);

        return ResponseEntity.ok( announceRepository.save(target));
    }

    /* 공지사항 삭제(비활성화) */
    @DeleteMapping("/{id}")
    public ResponseEntity<AnnounceBoardENT> delete(@PathVariable Long id, RedirectAttributes rttr) {
        AnnounceBoardENT target = announceRepository.findById(id).orElse(null);
        if (target != null) {
            target.toDisable();
            announceRepository.save(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        return ResponseEntity.ok(target);
    }
}
