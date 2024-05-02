package com.klinton.poc.storage.blob.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

@Service
public class S3StorageService {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final S3Client s3Client;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void storeMedia(String filePath, byte[] content) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        s3Client.putObject(objectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(content));
    }

    public void downloadMedia(String fileName) throws IOException {
        final var filePath = "images/%s".formatted(fileName);
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(filePath)
                .bucket(bucketName)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
        byte[] data = objectBytes.asByteArray();
        FileUtils.writeByteArrayToFile(new File(System.getProperty("user.dir") + "/src/main/resources/static/%s".formatted(fileName)), data);
    }
}
