package com.example.MultiGreenMaster.repository;


import com.example.MultiGreenMaster.entity.FreeBoardGoodENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FreeBoardGoodREP extends JpaRepository<FreeBoardGoodENT, Long> {
    @Query(value = "SELECT * FROM flower.freeboardgood where freeboard_id = :freeboardId AND user_id = :userId ", nativeQuery = true)
    Optional<FreeBoardGoodENT> findByFreeboardIdAndUserId(@Param("freeboardId") Long freeboardId, @Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) FROM flower.freeboardgood WHERE freeboard_id = :freeboardId", nativeQuery = true)
    Long countByFreeboardId(@Param("freeboardId") Long freeboardId);
}
