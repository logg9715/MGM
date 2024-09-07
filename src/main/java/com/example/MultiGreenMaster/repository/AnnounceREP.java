package com.example.MultiGreenMaster.repository;


import com.example.MultiGreenMaster.entity.AnnounceENT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnounceREP extends JpaRepository<AnnounceENT, Long> {
    List<AnnounceENT> findByDisableFalse();
}
