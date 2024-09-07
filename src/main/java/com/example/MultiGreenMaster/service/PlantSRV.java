package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.PlantsENT;
import com.example.MultiGreenMaster.repository.PlantsREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantSRV {

    private final PlantsREP plantsRepository;

    @Autowired
    public PlantSRV(PlantsREP plantsRepository) {
        this.plantsRepository = plantsRepository;
    }

    public void savePlantWithImages(String name, byte[] ypImage, byte[] mpImage) {
        PlantsENT plant = new PlantsENT();
        plant.setName(name);
        plant.setYP_picture(ypImage);
        plant.setMP_picture(mpImage);
        plantsRepository.save(plant);
    }

    public List<PlantsENT> findAll() {
        return plantsRepository.findAll();
    }
}