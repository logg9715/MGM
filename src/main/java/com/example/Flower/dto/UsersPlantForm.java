package com.example.Flower.dto;


import com.example.Flower.entity.Plants;
import com.example.Flower.entity.User;
import com.example.Flower.entity.UsersPlant;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
public class UsersPlantForm {
    private Long id;
    private String name; // 사용자 아이디
    private User userId; // 외래 키로 참조하는 Users 엔티티의 id
    private Plants plantsId; // 외래 키로 참조하는 Plants 엔티티의 id
    private LocalDateTime regdate;
    private LocalDateTime elapsed;
    private byte[] picture;
    private int wateredCount; // 물을 준 횟수
    private int fertilizeredCount; // 비료를 준 횟수
    private float humidity;
    private float temperature;

    public UsersPlant toEntity() {

        return new UsersPlant(id,name, userId, plantsId, regdate, elapsed, picture, wateredCount, fertilizeredCount, humidity, temperature);
    }

}
