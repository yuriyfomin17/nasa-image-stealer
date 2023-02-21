package com.nimofy.nasaimagestealer.repo;

import com.nimofy.nasaimagestealer.entities.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CameraRepository extends JpaRepository<Camera, Long> {
    @Transactional(readOnly = true)
    Optional<Camera> findCameraByCameraNasaId(Long cameraNasaId);

}
