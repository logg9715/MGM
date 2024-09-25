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
}
