package com.example.MultiGreenMaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plant")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlantENT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String ipaddress;

    /* 할당된 식물 코드 */
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserENT userENT;
}
