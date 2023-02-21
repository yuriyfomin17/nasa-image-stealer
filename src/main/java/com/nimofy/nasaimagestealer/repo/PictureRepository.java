package com.nimofy.nasaimagestealer.repo;

import com.nimofy.nasaimagestealer.entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    @Transactional(readOnly = true)
    @Query("SELECT EXISTS (select p from Picture p where p.pictureNasaId = ?1)")
    boolean pictureExistsByNasaId(Long pictureNasaId);
}
