package com.klinton.poc.storage.blob.service;

import com.klinton.poc.storage.blob.models.ImageMedia;
import com.klinton.poc.storage.blob.persistence.ImageMediaJpaRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3StorageService {

    @Value("${aws.s3.bucket.name}")
    private String BUCKET_NAME;

    private final S3Client s3Client;

    private final ImageMediaJpaRepository mediaRepository;

    public S3StorageService(final S3Client s3Client, final ImageMediaJpaRepository mediaRepository) {
        this.s3Client = Objects.requireNonNull(s3Client);
        this.mediaRepository = Objects.requireNonNull(mediaRepository);
    }

    @Transactional
    public void storeMedia(MultipartFile file) throws IOException {
        final var fileName = file.getOriginalFilename();
        final var filePath = "images/" + file.getOriginalFilename();
        final var content = file.getBytes();

        saveMetadata(fileName, filePath);

        storeAtCloudProvider(filePath, content);
    }

    public void downloadMedia(String fileName) throws IOException {
        final var filePath = "images/%s".formatted(fileName);
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(filePath)
                .bucket(BUCKET_NAME)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
        byte[] data = objectBytes.asByteArray();
        FileUtils.writeByteArrayToFile(new File(System.getProperty("user.dir") + "/src/main/resources/static/%s".formatted(fileName)), data);
    }

    private void saveMetadata(String fileName, String filePath) {
        final var id = UUID.randomUUID().toString();

        var imageMedia = ImageMedia.create(id, fileName, filePath);
        this.mediaRepository.save(imageMedia);
    }

    private void storeAtCloudProvider(String filePath, byte[] content) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(filePath)
                .build();

        s3Client.putObject(objectRequest, RequestBody.fromBytes(content));
    }
}
