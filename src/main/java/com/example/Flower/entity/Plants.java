package com.example.Flower.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Plants {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) //이 코드 때문에 더미 파일에 id값을 빼었다. 에러가 나기 때문.
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(name = "YP_picture", columnDefinition="LONGBLOB")
    private byte[] YP_picture;//어린 식물 예시 사진

    @Lob
    @Column(name = "MP_picture", columnDefinition="LONGBLOB")
    private byte[] MP_picture;//성숙한 식물 예시 사진

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getYP_picture() {
        return YP_picture;
    }

    public void setYP_picture(byte[] YP_picture) {
        this.YP_picture = YP_picture;
    }

    public byte[] getMP_picture() {
        return MP_picture;
    }

    public void setMP_picture(byte[] MP_picture) {
        this.MP_picture = MP_picture;
    }
}