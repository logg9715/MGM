package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.dto.MyPage_GrassFRM;
import com.example.MultiGreenMaster.entity.UserENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserREP extends JpaRepository<UserENT, Long> {
    Optional<UserENT> findByLoginId(String loginId);
    Optional<UserENT> findByNickname(String nickname);
    List<UserENT> findByDisableFalse();

    @Query(value = "SELECT DATE(f.regdate) AS regdate, COUNT(f.id) AS count " +
            "FROM freeboard f " +
            "WHERE f.user_id = :userId " +
            "AND f.regdate >= CURDATE() - INTERVAL 1 YEAR " +
            "GROUP BY DATE(f.regdate)",
            nativeQuery = true)
    List<Object[]> getGrassDateGroupByDateBefore1Year(@Param("userId") Long userId);


}
