package com.example.MultiGreenMaster.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "freeboardpicture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeBoardPictureENT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 사진 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private FreeBoardENT cmPost;  // 사진이 속한 게시글 정보

    @Lob
    @Column(name = "picture_data", columnDefinition = "LONGBLOB")
    private byte[] pictureData; // 사진 데이터 (LONGBLOB으로 저장)
}
