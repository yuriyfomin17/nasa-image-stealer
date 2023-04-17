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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Transactional
    public void getPicturesData(int sol) {
        var pictureUrl = buildURl(sol);

        var photosBody = restTemplate.getForObject(pictureUrl, NasaPhotos.class);
        Objects.requireNonNull(photosBody);
        var cameraListNasaPhotoMap = buildMapData(photosBody);
        saveDataToDataBase(cameraListNasaPhotoMap);
    }

    private Map<NasaVideoCamera, List<NasaPhoto>> buildMapData(NasaPhotos nasaPhotos) {
        return nasaPhotos.photos()
                .stream()
                .collect(Collectors.groupingBy(NasaPhoto::camera, Collectors.toList()));
    }
    public void saveDataToDataBase(Map<NasaVideoCamera, List<NasaPhoto>> cameraListNasaPhotoMap) {
        cameraListNasaPhotoMap.forEach((nasaVideoCamera, nasaPhotos) -> {
            var nasaCamera = findCamera(nasaVideoCamera);
            saveNasaPhotos(nasaPhotos, nasaCamera);
        });
    }

    private Camera findCamera(NasaVideoCamera nasaVideoCamera) {
        return cameraRepository.findCameraByCameraNasaId(nasaVideoCamera.id())
                .orElseGet(() -> cameraRepository.save(getNewCamera(nasaVideoCamera)));
    }
    private void saveNasaPhotos(List<NasaPhoto> nasaPhotos, Camera nasaCamera) {
        nasaPhotos.forEach(nasaPhoto -> handleNasaPhoto(nasaCamera, nasaPhoto));
    }

    private void handleNasaPhoto(Camera nasaCamera, NasaPhoto nasaPhoto) {
        if (!pictureRepository.existsByPictureNasaId(nasaPhoto.id())){
            Picture picture = new Picture();
            picture.setPictureNasaId(nasaPhoto.id());
            picture.setImgSrc(nasaPhoto.img_src());
            nasaCamera.addPicture(picture);
        }
    }

    private Camera getNewCamera(NasaVideoCamera nasaVideoCamera) {
        var camera = new Camera();
        camera.setCameraNasaId(nasaVideoCamera.id());
        camera.setName(nasaVideoCamera.name());
        return camera;
    }

    private URI buildURl(int sol) {
        return UriComponentsBuilder.fromHttpUrl(NASA_URL)
                .queryParam("sol", sol)
                .queryParam("api_key", NASA_KEY)
                .build().toUri();
    }

}
