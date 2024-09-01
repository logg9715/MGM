package com.example.Flower.controller;

import com.example.Flower.entity.Announce;
import com.example.Flower.entity.CMPost;
import com.example.Flower.repository.AnnounceRepository;
import com.example.Flower.repository.CMPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController extends SessionCheckController {

    @Autowired
    private AnnounceRepository announceRepository;

    @Autowired
    private CMPostRepository cmPostRepository;

    @GetMapping("/")
    public String home(Model model) {
        // 공지사항 목록 가져오기
        List<Announce> announceList = announceRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        // 최대 3개의 공지사항만 추출
        int maxAnnounceCount = 3;
        List<Announce> recentAnnounces = announceList.subList(0, Math.min(announceList.size(), maxAnnounceCount));

        // 최신 4개의 게시글을 오름차순으로 가져오기
        List<CMPost> recentPosts = cmPostRepository.findTop4ByOrderByRegdateDesc();

        // 모델에 추가
        model.addAttribute("announceList", recentAnnounces);
        model.addAttribute("postList", recentPosts); // 추출한 게시글을 모델에 추가

        return "main"; // main.mustache 템플릿으로 이동
    }

    @GetMapping("/streaming")
    public String streaming(Model model) {
        return "stream";
    }
}