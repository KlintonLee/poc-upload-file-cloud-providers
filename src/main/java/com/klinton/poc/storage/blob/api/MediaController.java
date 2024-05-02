package com.klinton.poc.storage.blob.api;

import com.klinton.poc.storage.blob.service.S3StorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
public class MediaController {

    private final S3StorageService s3StorageService;

    public MediaController(final S3StorageService s3StorageService) {
        this.s3StorageService = Objects.requireNonNull(s3StorageService);
    }

    @PostMapping("/upload")
    public void uploadMedia(@RequestBody MultipartFile file) throws IOException {
        s3StorageService.storeMedia(file);
    }

    @GetMapping("/download")
    public void downloadMedia(@RequestParam String fileName) throws IOException {
        s3StorageService.downloadMedia(fileName);
    }
}
