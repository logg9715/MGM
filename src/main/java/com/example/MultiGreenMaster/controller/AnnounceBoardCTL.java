package com.example.MultiGreenMaster.controller;
//공지사항 컨트롤러

import com.example.MultiGreenMaster.dto.AnnounceBoardFRM;
import com.example.MultiGreenMaster.entity.AnnounceBoardENT;
import com.example.MultiGreenMaster.repository.AnnounceREP;
import com.example.MultiGreenMaster.repository.UserREP;
import com.example.MultiGreenMaster.service.UserSRV;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class AnnounceBoardCTL extends SessionCheckCTL {
    @Autowired
    private AnnounceREP announceRepository;
    @Autowired
    private UserREP userRepository;
    @Autowired
    private UserSRV userService;

    /* 공지사항 목록 */
    @GetMapping("/announces")
    public String index(Model model) {
        List<AnnounceBoardENT> announceEntityList = (List<AnnounceBoardENT>) announceRepository.findByDisableFalse();
        model.addAttribute("announceList", announceEntityList);
        return "announces/index";
    }

    /* 새 공지사항 작성 */
    @GetMapping("/announces/new")
    public String newAnnounceForm(Model model) {
        return "announces/new";
    }

    /* 새 공지사항 제출 */
    @PostMapping("/announces/create")
    public String createAnnounce(AnnounceBoardFRM form, Model model) {  //DTO AnnounceForm
        AnnounceBoardENT announce = form.toEntity();
        AnnounceBoardENT saved = announceRepository.save(announce);
        return "redirect:/announces/"+saved.getId();
    }

    /* 공지사항 열람 */
    @GetMapping("/announces/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        AnnounceBoardENT announceEntity = announceRepository.findById(id).orElse(null);

        // 비활성화 된 글 접속 시도시 튕겨내기
        if(announceEntity.isDisable()) return "redirect:/announces";

        //show.mustache의 {{#announce}} {{/announce}}의 값
        model.addAttribute("announce", announceEntity);

        return "announces/show";
    }

    /* 공지사항 삭제(비활성화) */
    @GetMapping("/announces/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr, Model model) {
        AnnounceBoardENT target = announceRepository.findById(id).orElse(null);
        if (target != null) {
            target.toDisable();
            announceRepository.save(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        return "redirect:/announces";
    }
}
