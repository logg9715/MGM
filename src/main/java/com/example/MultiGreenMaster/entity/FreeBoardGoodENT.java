package com.example.MultiGreenMaster.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "freeboardgood")
@Builder
public class FreeBoardGoodENT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freeboardId")
    private FreeBoardENT freeboardid; // 게시글 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserENT userid; // 유저 정보

}
