package com.example.MultiGreenMaster.dto;


import com.example.MultiGreenMaster.entity.PlantsENT;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class PlantsFRM {
    private Long id;
    private String name;
    private byte[] YP_picture;
    private byte[] MP_picture;

    public PlantsENT toEntity() {

        return new PlantsENT(id,name, YP_picture, MP_picture);
    }

}
