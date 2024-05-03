package com.klinton.poc.store.objects.service;

import com.klinton.poc.store.objects.exceptions.NotFoundException;
import com.klinton.poc.store.objects.models.ImageMedia;
import com.klinton.poc.store.objects.persistence.ImageMediaJpaRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

    public List<ImageMedia> listImages() {
        return this.mediaRepository.findAll();
    }

    public void downloadMedia(String id) throws IOException {
        ImageMedia imageMedia = checkImageMetadataExists(id);

        final var filePath = imageMedia.getLocation();
        byte[] objectBytes = getObjectAtCloudProvider(filePath)
                .asByteArray();

        final var pathToDownload = System.getProperty("user.dir") + "/assets/%s".formatted(imageMedia.getName());
        saveFile(objectBytes, pathToDownload);
    }

    public void deleteMedia(String id) {
        ImageMedia imageMedia = checkImageMetadataExists(id);
        final var filePath = imageMedia.getLocation();

        final var objectRequest = DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(filePath).build();
        s3Client.deleteObject(objectRequest);

        this.mediaRepository.delete(imageMedia);
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

        final var temp = s3Client.putObject(objectRequest, RequestBody.fromBytes(content));
        System.out.println(temp);
    }

    private ImageMedia checkImageMetadataExists(String id) {
        return this.mediaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Image with id: %s was not found".formatted(id)));
    }

    private ResponseBytes<GetObjectResponse> getObjectAtCloudProvider(String filePath) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(filePath)
                .bucket(BUCKET_NAME)
                .build();

        return s3Client.getObjectAsBytes(objectRequest);
    }

    private static void saveFile(byte[] objectBytes, String pathToBeSaved) throws IOException {
        FileUtils.writeByteArrayToFile(new File(pathToBeSaved), objectBytes);
    }
}
