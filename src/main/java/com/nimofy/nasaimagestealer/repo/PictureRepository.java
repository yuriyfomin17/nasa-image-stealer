package com.nimofy.nasaimagestealer.repo;

import com.nimofy.nasaimagestealer.entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    @Transactional(readOnly = true)
    boolean existsByPictureNasaId(Long pictureNasaId);

}
