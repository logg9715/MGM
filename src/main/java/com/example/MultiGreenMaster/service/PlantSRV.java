package com.example.MultiGreenMaster.service;

import com.example.MultiGreenMaster.dto.PlantFRM;
import com.example.MultiGreenMaster.entity.PlantENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.repository.PlantREP;
import com.example.MultiGreenMaster.repository.UserREP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantSRV {
    @Autowired
    private PlantREP plantREP;

    @Autowired
    private UserREP userREP;

    public PlantENT findByUserId (Long userId) {
        return this.plantREP.findByUserId(userId).orElse(null);
    }

    /* 식물 정보 등록&업데이트 */
    public PlantENT updatePlant(PlantFRM plantFRM) {
        Long userId = plantFRM.getUser_id();
        PlantENT plantENT;

        if (userId != null) {
            UserENT tmpUser = userREP.findById(userId).orElse(null);
            plantENT = plantFRM.toEntity(tmpUser);
            System.out.println("print plantEnt : " + plantENT);
        } else {
            plantENT = plantFRM.toEntity(null);
        }

        return plantREP.save(plantENT);
    }

    public List<PlantFRM> findAllOnForm() {
        return plantREP.findAllOnForm();
    }

}
