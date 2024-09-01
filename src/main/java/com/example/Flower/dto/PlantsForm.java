package com.example.Flower.dto;


import com.example.Flower.entity.Plants;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class PlantsForm {
    private Long id;
    private String name;
    private byte[] YP_picture;
    private byte[] MP_picture;

    public Plants toEntity() {

        return new Plants(id,name, YP_picture, MP_picture);
    }

}
