package com.klinton.poc.store.objects.service;

import com.klinton.poc.store.objects.exceptions.NotFoundException;
import com.klinton.poc.store.objects.models.CloudProvider;
import com.klinton.poc.store.objects.models.ImageMedia;
import com.klinton.poc.store.objects.persistence.ImageMediaJpaRepository;
import com.klinton.poc.store.objects.persistence.S3StorageGatewayImpl;
import com.klinton.poc.store.objects.persistence.StorageGateway;
import com.klinton.poc.store.objects.presenters.ImageBase64;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ImageMediaService {

    private final ImageMediaJpaRepository mediaRepository;

    private final StorageGateway s3StorageGateway;

    private final StorageGateway googleStorageGateway;

    public ImageMediaService(
            final ImageMediaJpaRepository mediaRepository,
            final S3StorageGatewayImpl storageGateway,
            final StorageGateway googleStorageGateway) {
        this.mediaRepository = Objects.requireNonNull(mediaRepository);
        this.s3StorageGateway = Objects.requireNonNull(storageGateway);
        this.googleStorageGateway = Objects.requireNonNull(googleStorageGateway);
    }

    public void save(MultipartFile file, CloudProvider providerEnum) throws IOException {
        final var fileName = file.getOriginalFilename();
        final var filePath = "images/" + file.getOriginalFilename();
        final var contentType = file.getContentType();
        final var content = file.getBytes();

        saveMetadata(fileName, contentType, filePath, providerEnum);

        if (providerEnum == CloudProvider.AWS) {
            s3StorageGateway.storeFile(filePath, contentType, content);
        } else if (providerEnum == CloudProvider.GOOGLE) {
            googleStorageGateway.storeFile(filePath, contentType, content);
        }
    }

    public List<ImageMedia> list() {
        return this.mediaRepository.findAll();
    }

    public ImageBase64 getBase64(String id) {
        var imageMedia = checkImageMetadataExists(id);
        final var filePath = imageMedia.getLocation();
        final var provider = CloudProvider.valueOf(imageMedia.getProvider());
        byte[] objectBytes;

        if (provider == CloudProvider.AWS) {
            objectBytes = s3StorageGateway.getFile(filePath);
        } else {
            objectBytes = googleStorageGateway.getFile(filePath);
        }

        final var base64Data = Base64.getEncoder().encodeToString(objectBytes);
        return ImageBase64.create(imageMedia.getContentType(), imageMedia.getName(), base64Data);
    }

    public void downloadMedia(String id) throws IOException {
        ImageMedia imageMedia = checkImageMetadataExists(id);
        final var filePath = imageMedia.getLocation();
        final var provider = CloudProvider.valueOf(imageMedia.getProvider());
        byte[] objectBytes;

        if (provider == CloudProvider.AWS) {
            objectBytes = s3StorageGateway.getFile(filePath);
        } else {
            objectBytes = googleStorageGateway.getFile(filePath);
        }

        final var pathToDownload = System.getProperty("user.dir") + "/assets/%s".formatted(imageMedia.getName());
        saveFile(objectBytes, pathToDownload);
    }

    public void delete(String id) {
        ImageMedia imageMedia = checkImageMetadataExists(id);
        final var filePath = imageMedia.getLocation();
        final var provider = CloudProvider.valueOf(imageMedia.getProvider());

        if (provider == CloudProvider.AWS) {
            s3StorageGateway.deleteFile(filePath);
        } else {
            googleStorageGateway.getFile(filePath);
        }

        this.mediaRepository.delete(imageMedia);
    }

    private void saveMetadata(String fileName, String contentType, String filePath, CloudProvider provider) {
        final var id = UUID.randomUUID().toString();

        var imageMedia = ImageMedia.create(id, fileName, contentType, provider.toString(), filePath);
        this.mediaRepository.save(imageMedia);
    }

    private ImageMedia checkImageMetadataExists(String id) {
        return this.mediaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Image with id: %s was not found".formatted(id)));
    }

    private static void saveFile(byte[] objectBytes, String pathToBeSaved) throws IOException {
        FileUtils.writeByteArrayToFile(new File(pathToBeSaved), objectBytes);
    }
}