package com.example.MultiGreenMaster.api;
// 공지사항 api 컨트롤러
import com.example.MultiGreenMaster.controller.SessionCheckController;
import com.example.MultiGreenMaster.dto.AnnounceForm;
import com.example.MultiGreenMaster.entity.Announce;
import com.example.MultiGreenMaster.repository.AnnounceRepository;
import com.example.MultiGreenMaster.repository.UserRepository;
import com.example.MultiGreenMaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/announces")
public class AnnounceApiController extends SessionCheckController {
    @Autowired
    private AnnounceRepository announceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    /* 공지사항 목록 */
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping
    public ResponseEntity<List<Announce>> index() {
        List<Announce> announceEntityList = (List<Announce>) announceRepository.findByDisableFalse();
        return ResponseEntity.ok(announceEntityList);
    }

    /* 새 공지사항 제출 */
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/create")
    public ResponseEntity<Announce> createAnnounce(AnnounceForm form, Model model) {  //DTO AnnounceForm
        Announce announce = form.toEntity();
        Announce saved = announceRepository.save(announce);
        return ResponseEntity.ok(saved);
    }

    /* 공지사항 열람 */
    @GetMapping("/{id}")
    public ResponseEntity<Announce> show(@PathVariable("id") Long id) {
        Announce announceEntity = announceRepository.findById(id).orElse(null);
        return ResponseEntity.ok(announceEntity);
    }

    /* 공지사항 삭제(비활성화) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Announce> delete(@PathVariable Long id, RedirectAttributes rttr) {
        Announce target = announceRepository.findById(id).orElse(null);
        if (target != null) {
            target.toDisable();
            announceRepository.save(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        return ResponseEntity.ok(target);
    }
}
