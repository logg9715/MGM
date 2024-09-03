package com.example.MultiGreenMaster.repository;


import com.example.MultiGreenMaster.entity.Announce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnounceRepository extends JpaRepository<Announce, Long> {
    List<Announce> findByDisableFalse(); 
}
