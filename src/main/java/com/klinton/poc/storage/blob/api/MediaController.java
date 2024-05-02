package com.klinton.poc.storage.blob.api;

import com.klinton.poc.storage.blob.service.S3StorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
public class MediaController {

    private final S3StorageService s3StorageService;

    public MediaController(S3StorageService s3StorageService) {
        this.s3StorageService = Objects.requireNonNull(s3StorageService);
    }

    @PostMapping("/upload")
    public void uploadMedia(@RequestBody MultipartFile file) throws IOException {
        final var filePath = "images/" + file.getOriginalFilename();
        s3StorageService.storeMedia(filePath, file.getBytes());
    }
}
