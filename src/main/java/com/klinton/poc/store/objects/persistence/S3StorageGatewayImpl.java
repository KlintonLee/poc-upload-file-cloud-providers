package com.klinton.poc.store.objects.persistence;

import com.klinton.poc.store.objects.configuration.S3StorageProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Objects;

@Service
public class S3StorageGatewayImpl implements StorageGateway {

    private final S3StorageProperties s3Props;

    private final S3Client s3Client;

    public S3StorageGatewayImpl(final S3StorageProperties s3Props, final S3Client s3Client) {
        this.s3Props = Objects.requireNonNull(s3Props);
        this.s3Client = Objects.requireNonNull(s3Client);
    }

    @Override
    public void storeFile(String filePath, String contentType, byte[] content) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(s3Props.getBucket())
                .key(filePath)
                .contentType(contentType)
                .build();

        s3Client.putObject(objectRequest, RequestBody.fromBytes(content));
    }

    @Override
    public byte[] getFileBytes(String filePath) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(filePath)
                .bucket(s3Props.getBucket())
                .build();

        return s3Client.getObjectAsBytes(objectRequest)
                .asByteArray();
    }

    @Override
    public void deleteFile(String filePath) {
        final var objectRequest = DeleteObjectRequest
                .builder()
                .bucket(s3Props.getBucket())
                .key(filePath)
                .build();

        s3Client.deleteObject(objectRequest);
    }
}
