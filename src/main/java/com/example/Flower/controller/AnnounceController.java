package com.example.Flower.controller;


import com.example.Flower.dto.AnnounceForm;
import com.example.Flower.entity.Announce;
import com.example.Flower.repository.AnnounceRepository;
import com.example.Flower.repository.UserRepository;
import com.example.Flower.service.UserService;
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
public class AnnounceController extends SessionCheckController{
    //공지사항 컨트롤러
    //01. 의존성 주입
    @Autowired
    private AnnounceRepository announceRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/announces")
    public String index(Model model) {
        List<Announce> announceEntityList = (List<Announce>) announceRepository.findAll();
        model.addAttribute("announceList", announceEntityList);
        return "announces/index";
    }

    @GetMapping("/announces/new")
    public String newAnnounceForm(Model model) {
        return "announces/new";
    }

    @PostMapping("/announces/create")
    public String createAnnounce(AnnounceForm form, Model model) {  //DTO AnnounceForm

        //System.out.println(form.toString());
        log.info(form.toString());

        // 1. DTO를 엔티티로 변환
        Announce announce = form.toEntity();
        log.info(announce.toString());
        //System.out.println(announce.toString());

        Announce saved = announceRepository.save(announce);
        //System.out.println(saved.toString());
        log.info(saved.toString());
        return "redirect:/announces/"+saved.getId();
    }

    @GetMapping("/announces/{id}")
    public String show(@PathVariable("id") Long id, Model model) {

        log.info("id = " + id);
        Announce announceEntity = announceRepository.findById(id).orElse(null);
        model.addAttribute("announce", announceEntity);//show.mustache의 {{#announce}} {{/announce}}의 값

        //List<CommentDto> commentsDtos = commentService.comments(id);
        //model.addAttribute("commentDtos",commentsDtos);

        return "announces/show";//show.mustache 리턴
    }

    @GetMapping("/announces/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr, Model model) {
        Announce target = announceRepository.findById(id).orElse(null);
        if (target != null) {
            announceRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        return "redirect:/announces";
    }
}
