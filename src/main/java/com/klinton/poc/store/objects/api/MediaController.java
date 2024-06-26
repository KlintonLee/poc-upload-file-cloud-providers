package com.klinton.poc.store.objects.api;

import com.klinton.poc.store.objects.exceptions.UnprocessableEntity;
import com.klinton.poc.store.objects.models.CloudProvider;
import com.klinton.poc.store.objects.models.ImageMedia;
import com.klinton.poc.store.objects.presenters.ImageBase64;
import com.klinton.poc.store.objects.service.ImageMediaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/images")
public class MediaController {

    private final ImageMediaService mediaService;

    public MediaController(final ImageMediaService mediaService) {
        this.mediaService = Objects.requireNonNull(mediaService);
    }

    @PostMapping("/upload")
    public void uploadMedia(
            @RequestBody MultipartFile file,
            @RequestParam(defaultValue = "gcp") String provider
    ) throws IOException {
        var providerEnum = getCloudProvider(provider);
        mediaService.save(file, providerEnum);
    }

    @GetMapping
    public List<ImageMedia> listImages() {
        return mediaService.list();
    }

    @GetMapping("/base64/{id}")
    public ImageBase64 getImageBase64(@PathVariable String id) {
        return mediaService.getBase64(id);
    }

    @GetMapping("/download/{id}")
    public void downloadMedia(@PathVariable String id) throws IOException {
        mediaService.downloadMedia(id);
    }

    @DeleteMapping("/{id}")
    public void deleteMedia(@PathVariable String id) {
        mediaService.delete(id);
    }

    private static CloudProvider getCloudProvider(String providerName) {
        return CloudProvider.findByProvider(providerName)
                .orElseThrow(() -> new UnprocessableEntity("Invalid provider, make sure to choose between aws and gcp"));
    }
}
