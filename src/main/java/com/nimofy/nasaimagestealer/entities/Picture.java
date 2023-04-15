package com.nimofy.nasaimagestealer.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pictures")
@Getter
@Setter
@EqualsAndHashCode(of = "pictureNasaId")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "camera_id")
    private Camera camera;

    @Column(name = "picture_nasa_id")
    private Long pictureNasaId;

    @Column(name = "img_src")
    private String imgSrc;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
