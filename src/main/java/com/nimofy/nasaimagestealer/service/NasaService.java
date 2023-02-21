package com.nimofy.nasaimagestealer.service;

import com.nimofy.nasaimagestealer.dto.NasaPhoto;
import com.nimofy.nasaimagestealer.dto.NasaPhotos;
import com.nimofy.nasaimagestealer.dto.NasaVideoCamera;
import com.nimofy.nasaimagestealer.entities.Camera;
import com.nimofy.nasaimagestealer.entities.Picture;
import com.nimofy.nasaimagestealer.repo.CameraRepository;
import com.nimofy.nasaimagestealer.repo.PictureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasaService {

    private final RestTemplate restTemplate;
    private final CameraRepository cameraRepository;
    private final PictureRepository pictureRepository;
    @Value("${nasa.api.url}")
    private String NASA_URL;

    @Value("${nasa.api.key}")
    private String NASA_KEY;

    public void getPicturesData(int sol) {
        var pictureUrl = buildURl(sol);

        var photos = restTemplate.getForEntity(pictureUrl, NasaPhotos.class).getBody();
        assert photos != null;
        var cameraListNasaPhotoMap = buildMapData(photos);
        saveDataToDataBase(cameraListNasaPhotoMap);
    }

    private Map<NasaVideoCamera, List<NasaPhoto>> buildMapData(NasaPhotos nasaPhotos) {
        Map<NasaVideoCamera, List<NasaPhoto>> cameraListMap = new ConcurrentHashMap<>();
        nasaPhotos.photos().forEach(nasaPhoto -> cameraListMap.computeIfAbsent(nasaPhoto.camera(), k -> new ArrayList<>())
                .add(nasaPhoto));
        return cameraListMap;
    }

    @Transactional
    public void saveDataToDataBase(Map<NasaVideoCamera, List<NasaPhoto>> cameraListNasaPhotoMap) {
        cameraListNasaPhotoMap.forEach((nasaVideoCamera, nasaPhotos) -> {
            var nasaCamera = findCamera(nasaVideoCamera.id());
            if (nasaCamera.getCameraNasaId() == null) {
                nasaCamera.setCameraNasaId(nasaVideoCamera.id());
                nasaCamera.setName(nasaVideoCamera.name());
            }

            nasaPhotos.forEach(nasaPhoto -> {
                if (!pictureRepository.pictureExistsByNasaId(nasaPhoto.id())) {
                    Picture picture = new Picture();

                    picture.setPictureNasaId(nasaPhoto.id());
                    nasaCamera.addPicture(picture);
                    picture.setCamera(nasaCamera);

                    picture.setImgSrc(nasaPhoto.img_src());
                }
            });
            if (saveCameraOrPictureList(nasaCamera)) {
                cameraRepository.saveAndFlush(nasaCamera);
            }
        });
    }

    private Camera findCamera(Long nasaVideoCameraId) {
        return cameraRepository.findCameraByCameraNasaId(nasaVideoCameraId).orElseGet(Camera::new);
    }

    private boolean saveCameraOrPictureList(Camera camera) {
        return camera.getId() == null || camera.getPictures().size() >= 1;
    }

    private URI buildURl(int sol) {
        return UriComponentsBuilder.fromHttpUrl(NASA_URL)
                .queryParam("sol", sol)
                .queryParam("api_key", NASA_KEY)
                .build().toUri();
    }

}
