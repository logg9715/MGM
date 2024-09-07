package com.example.MultiGreenMaster.repository;


import com.example.MultiGreenMaster.entity.AnnounceBoardENT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnounceREP extends JpaRepository<AnnounceBoardENT, Long> {
    List<AnnounceBoardENT> findByDisableFalse();
}
