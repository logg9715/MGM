package com.example.MultiGreenMaster.api;
// 공지사항 api 컨트롤러
import com.example.MultiGreenMaster.controller.SessionCheckController;
import com.example.MultiGreenMaster.entity.Announce;
import com.example.MultiGreenMaster.repository.AnnounceRepository;
import com.example.MultiGreenMaster.repository.UserRepository;
import com.example.MultiGreenMaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<Announce>> index(Model model) {
        List<Announce> announceEntityList = (List<Announce>) announceRepository.findByDisableFalse();
        return ResponseEntity.ok(announceEntityList);
    }
}
