package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.dto.AnnounceBoardFRM;
import com.example.MultiGreenMaster.entity.AnnounceBoardENT;
import com.example.MultiGreenMaster.repository.AnnounceBoardREP;
import com.example.MultiGreenMaster.repository.UserREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnounceBoardSRV {
    @Autowired
    private AnnounceBoardREP announceRepository;
    @Autowired
    private UserREP userRepository;
    @Autowired
    private UserSRV userService;

    public List<AnnounceBoardENT> getAllEnableAnnounces() {
        return (List<AnnounceBoardENT>) announceRepository.findByDisableFalse();
    }

    public AnnounceBoardENT saveNewAnnounce(AnnounceBoardFRM announceBoardFRM) {
        AnnounceBoardENT announce = announceBoardFRM.toEntity();
        return announceRepository.save(announce);
    }

    public AnnounceBoardENT findAnnounceById(Long id) {
        return announceRepository.findById(id).orElse(null);
    }

    /* 최근 4개 공지사항 조회 */
    public List<AnnounceBoardENT> getRecentAnnounces() {
        return announceRepository.findTop4ByDisableFalseOrderByCreatedDateDesc();
    }

    /* 공지사항 수정 */
    public AnnounceBoardENT updateAnnounce(Long id, AnnounceBoardFRM form) {
        AnnounceBoardENT existingAnnounce = announceRepository.findById(id).orElse(null);
        if (existingAnnounce != null) {
            AnnounceBoardENT newData = form.toEntity();
            System.out.println("@@@@ : 폼 id " + newData.getContent());
            existingAnnounce.patch(newData); // 기존 공지사항에 새로운 데이터를 적용
            return announceRepository.save(existingAnnounce); // 수정된 공지사항을 저장하고 반환
        }
        return null;
    }

    /* 공지사항 비활성화 */
    public AnnounceBoardENT deleteAnnounce(Long id) {
        AnnounceBoardENT existingAnnounce = announceRepository.findById(id).orElse(null);
        if (existingAnnounce != null) {
            existingAnnounce.toDisable(); // 공자사항 비활성화 하기
            return announceRepository.save(existingAnnounce);
        }
        return null;
    }
}
