package com.nimofy.nasaimagestealer.api;

import com.nimofy.nasaimagestealer.dto.SolRequest;
import com.nimofy.nasaimagestealer.service.NasaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NasaApi {
    private final NasaService nasaService;

    @PostMapping("pictures/steal")
    public ResponseEntity<Void> stealImage(@RequestBody SolRequest solRequest) {
        nasaService.getPicturesData(solRequest.sol());
        return ResponseEntity.ok().build();
    }
}
