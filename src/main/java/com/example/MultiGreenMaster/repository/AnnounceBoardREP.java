package com.example.MultiGreenMaster.repository;


import com.example.MultiGreenMaster.entity.AnnounceBoardENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnnounceBoardREP extends JpaRepository<AnnounceBoardENT, Long> {
    List<AnnounceBoardENT> findByDisableFalse();
    // disable이 false인 데이터 중에서 최근 4개를 가져오기
    @Query("SELECT a FROM AnnounceBoardENT a WHERE a.disable = false ORDER BY a.id DESC LIMIT 4")
    List<AnnounceBoardENT> findTop4ByDisableFalseOrderByCreatedDateDesc();
}
