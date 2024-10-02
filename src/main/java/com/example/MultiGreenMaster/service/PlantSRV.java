package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.entity.PlantENT;
import com.example.MultiGreenMaster.repository.PlantREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantSRV {
    @Autowired
    private PlantREP plantREP;

    public PlantENT findById(Long id) {
        return this.plantREP.findById(id).orElse(null);
    }

    public PlantENT findByUserId (Long userId) {
        return this.plantREP.findByUserId(userId).orElse(null);
        }
}
