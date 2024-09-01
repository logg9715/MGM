package com.example.Flower.repository;

import com.example.Flower.entity.ScreenShot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface ScreenShotRepository extends JpaRepository<ScreenShot, Long> {
}
