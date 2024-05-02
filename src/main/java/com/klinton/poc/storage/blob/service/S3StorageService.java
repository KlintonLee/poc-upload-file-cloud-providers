package com.klinton.poc.storage.blob.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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
}
