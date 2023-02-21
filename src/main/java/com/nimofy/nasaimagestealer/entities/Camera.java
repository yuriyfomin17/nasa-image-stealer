package com.nimofy.nasaimagestealer.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cameras")
@Getter
@Setter
@EqualsAndHashCode(of = "cameraNasaId")
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @OneToMany(mappedBy = "camera", cascade = CascadeType.ALL)
    private List<Picture> pictures = new ArrayList<>();
    @Column(name = "camera_nasa_id")
    private Long cameraNasaId;
    @Column(name = "name")
    private String name;
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public void addPicture(Picture picture) {
        pictures.add(picture);
        picture.setCamera(this);

    }

    public void removePicture(Picture picture) {
        pictures.remove(picture);
        picture.setCamera(null);

    }
}
