package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.FreeBoardENT;
import com.example.MultiGreenMaster.entity.FreeBoardGoodENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.repository.FreeBoardGoodREP;
import com.example.MultiGreenMaster.repository.FreeBoardREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FreeBoardGoodSRV {
    @Autowired
    private FreeBoardGoodREP freeBoardGoodREP;

    /* 게시글 추천하기 (이미 했다면 false 반환) */
    public boolean recommend(FreeBoardENT freeBoardENT, UserENT userENT) {
        FreeBoardGoodENT target = freeBoardGoodREP.findByFreeboardIdAndUserId(freeBoardENT.getId(), userENT.getId()).orElse(null);
        /* 이미 추천 했다면 튕겨냄 */
        if(target != null) return false;

        freeBoardGoodREP.save(FreeBoardGoodENT.builder().freeboardid(freeBoardENT).userid(userENT).build());
        return true;
    }

    public boolean isRecommended(FreeBoardENT freeBoardENT, UserENT userENT) {
        FreeBoardGoodENT target = freeBoardGoodREP.findByFreeboardIdAndUserId(freeBoardENT.getId(), userENT.getId()).orElse(null);

        return (target == null) ? false : true;
    }
}
