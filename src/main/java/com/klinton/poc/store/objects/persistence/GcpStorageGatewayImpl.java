package com.klinton.poc.store.objects.persistence;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.klinton.poc.store.objects.configuration.GoogleStorageProperties;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GcpStorageGatewayImpl implements StorageGateway {

    private final GoogleStorageProperties storageProperties;

    private final Storage storage;

    public GcpStorageGatewayImpl(final GoogleStorageProperties storageProperties, final Storage storage) {
        this.storageProperties = Objects.requireNonNull(storageProperties);
        this.storage = Objects.requireNonNull(storage);
    }

    @Override
    public void storeFile(String filePath, String contentType, byte[] content) {
        var blobId = BlobId.of(storageProperties.getBucket(), Objects.requireNonNull(filePath));
        var blobInfo = BlobInfo
                .newBuilder(blobId)
                .setContentType(contentType)
                .build();

        storage.create(blobInfo, content);
    }

    @Override
    public byte[] getFileBytes(String filePath) {
        var blobId = BlobId.of(storageProperties.getBucket(), Objects.requireNonNull(filePath));
        return storage.readAllBytes(blobId);
    }

    @Override
    public void deleteFile(String filePath) {
        var blobId = BlobId.of(storageProperties.getBucket(), Objects.requireNonNull(filePath));
        storage.delete(blobId);
    }
}
