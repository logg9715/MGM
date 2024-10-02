package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.PlantENT;
import com.example.MultiGreenMaster.entity.UserENT;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlantFRM {
    private Long id;
    private String ipaddress;
    private Long user_id;

    public PlantENT toEntity(UserENT userENT) {
        return PlantENT.builder()
                .id(this.id)
                .ipaddress(this.ipaddress)
                .userENT(userENT)
                .build();
    }
}
