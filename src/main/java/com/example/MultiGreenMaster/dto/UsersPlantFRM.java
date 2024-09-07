package com.example.MultiGreenMaster.dto;


import com.example.MultiGreenMaster.entity.PlantsENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.entity.User_PlantENT;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
public class UsersPlantFRM {
    private Long id;
    private String name; // 사용자 아이디
    private UserENT userId; // 외래 키로 참조하는 Users 엔티티의 id
    private PlantsENT plantsId; // 외래 키로 참조하는 Plants 엔티티의 id
    private LocalDateTime regdate;
    private LocalDateTime elapsed;
    private byte[] picture;
    private int wateredCount; // 물을 준 횟수
    private int fertilizeredCount; // 비료를 준 횟수
    private float humidity;
    private float temperature;

    public User_PlantENT toEntity() {

        return new User_PlantENT(id,name, userId, plantsId, regdate, elapsed, picture, wateredCount, fertilizeredCount, humidity, temperature);
    }

}
