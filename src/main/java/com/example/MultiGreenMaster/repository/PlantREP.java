package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.dto.UserDTO_id_nickname;
import com.example.MultiGreenMaster.entity.PlantENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlantREP extends JpaRepository<PlantENT, Long> {
    @Query(value = "SELECT * FROM flower.plant where user_id = :userId", nativeQuery = true)
    Optional<PlantENT> findByUserId(@Param("userId") Long userId);
}
