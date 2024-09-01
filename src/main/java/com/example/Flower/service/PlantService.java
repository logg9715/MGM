package com.example.Flower.service;

import com.example.Flower.entity.Plants;
import com.example.Flower.repository.PlantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

@Service
public class PlantService {

    private final PlantsRepository plantsRepository;

    @Autowired
    public PlantService(PlantsRepository plantsRepository) {
        this.plantsRepository = plantsRepository;
    }

    public void savePlantWithImages(String name, byte[] ypImage, byte[] mpImage) {
        Plants plant = new Plants();
        plant.setName(name);
        plant.setYP_picture(ypImage);
        plant.setMP_picture(mpImage);
        plantsRepository.save(plant);
    }

    public List<Plants> findAll() {
        return plantsRepository.findAll();
    }
}