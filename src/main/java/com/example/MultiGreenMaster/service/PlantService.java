package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.Plants;
import com.example.MultiGreenMaster.repository.PlantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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