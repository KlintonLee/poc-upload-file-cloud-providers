package com.klinton.poc.store.objects.api;

import com.klinton.poc.store.objects.models.ImageMedia;
import com.klinton.poc.store.objects.service.S3StorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/images")
public class MediaController {

    private final S3StorageService s3StorageService;

    public MediaController(final S3StorageService s3StorageService) {
        this.s3StorageService = Objects.requireNonNull(s3StorageService);
    }

    @PostMapping("/upload")
    public void uploadMedia(@RequestBody MultipartFile file) throws IOException {
        s3StorageService.storeMedia(file);
    }

    @GetMapping
    public List<ImageMedia> listImages() {
        return s3StorageService.listImages();
    }

    @GetMapping("/download/{id}")
    public void downloadMedia(@PathVariable String id) throws IOException {
        s3StorageService.downloadMedia(id);
    }

    @DeleteMapping("/{id}")
    public void deleteMedia(@PathVariable String id) {
        s3StorageService.deleteMedia(id);
    }
}
