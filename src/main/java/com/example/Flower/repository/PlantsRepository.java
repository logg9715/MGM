package com.example.Flower.repository;

import com.example.Flower.entity.Plants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PlantsRepository extends JpaRepository<Plants, Long> {

}
