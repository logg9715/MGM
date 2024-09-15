package com.example.MultiGreenMaster.controller;

import com.example.MultiGreenMaster.entity.DiaryBoardENT;
import com.example.MultiGreenMaster.exeption.DiaryDeleteExcption;
import com.example.MultiGreenMaster.exeption.DiaryNotFoundException;
import com.example.MultiGreenMaster.service.DiaryBoardSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DiaryBoardCTL extends SessionCheckCTL
{

    @Autowired
    private DiaryBoardSRV diaryService;

    // 일기장 목록 페이지 매핑
    @GetMapping("/diaries")
    public String diaryList(Model model) {
        // 필요한 데이터를 model에 추가하고 "diary/diaryList" 템플릿으로 이동
        return "diary/diaryList";
    }

    // 일기장 작성 페이지 매핑
    @GetMapping("/diaries/create")
    public String diaryForm(Model model) {
        // 일기장 작성에 필요한 데이터를 추가하고 "diary/diaryForm" 템플릿으로 이동
        return "diary/diaryForm";
    }

    // 일기장 상세 조회 페이지 매핑
    @GetMapping("/diaries/{id}")
    public String diaryDetail(@PathVariable Long id, Model model) {
        try {
            DiaryBoardENT diary = diaryService.findDiaryById(id);
            model.addAttribute("diary", diary);
            return "diary/diaryDetail"; // Mustache 템플릿 이름
        } catch (DiaryDeleteExcption | DiaryNotFoundException e) {
            return "redirect:/diaries"; // 오류 발생 시 리스트 페이지로 리다이렉트
        }
    }


}