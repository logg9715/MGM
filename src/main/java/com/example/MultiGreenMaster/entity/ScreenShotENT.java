package com.example.MultiGreenMaster.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "screenshot")
public class ScreenShotENT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")//not null
    private byte[] image;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}