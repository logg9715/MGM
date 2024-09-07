package com.example.MultiGreenMaster.api;
// 공지사항 api 컨트롤러
import com.example.MultiGreenMaster.controller.SessionCheckCTL;
import com.example.MultiGreenMaster.dto.AnnounceFRM;
import com.example.MultiGreenMaster.entity.AnnounceENT;
import com.example.MultiGreenMaster.repository.AnnounceREP;
import com.example.MultiGreenMaster.repository.UserREP;
import com.example.MultiGreenMaster.service.UserSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/announces")
public class AnnounceAPI extends SessionCheckCTL {
    @Autowired
    private AnnounceREP announceRepository;
    @Autowired
    private UserREP userRepository;
    @Autowired
    private UserSRV userService;

    /* 공지사항 목록 */
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<AnnounceENT>> index() {
        List<AnnounceENT> announceEntityList = (List<AnnounceENT>) announceRepository.findByDisableFalse();
        return ResponseEntity.ok(announceEntityList);
    }

    /* 새 공지사항 제출 */
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/create")
    public ResponseEntity<AnnounceENT> createAnnounce(AnnounceFRM form, Model model) {  //DTO AnnounceForm
        AnnounceENT announce = form.toEntity();
        AnnounceENT saved = announceRepository.save(announce);
        return ResponseEntity.ok(saved);
    }

    /* 공지사항 열람 */
    @GetMapping("/{id}")
    public ResponseEntity<AnnounceENT> show(@PathVariable("id") Long id) {
        AnnounceENT announceEntity = announceRepository.findById(id).orElse(null);
        return ResponseEntity.ok(announceEntity);
    }

    /* 공지사항 삭제(비활성화) */
    @DeleteMapping("/{id}")
    public ResponseEntity<AnnounceENT> delete(@PathVariable Long id, RedirectAttributes rttr) {
        AnnounceENT target = announceRepository.findById(id).orElse(null);
        if (target != null) {
            target.toDisable();
            announceRepository.save(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        return ResponseEntity.ok(target);
    }
}
