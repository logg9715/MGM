package com.example.MultiGreenMaster.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MyPage_GrassFRM {
    LocalDateTime regdate;
    Long count;
}
