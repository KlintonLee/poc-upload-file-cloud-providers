package com.klinton.poc.store.objects.persistence;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.klinton.poc.store.objects.configuration.GoogleStorageProperties;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GoogleStorageGatewayImpl implements StorageGateway {

    private final GoogleStorageProperties storageProperties;

    private final Storage storage;

    public GoogleStorageGatewayImpl(final GoogleStorageProperties storageProperties, final Storage storage) {
        this.storageProperties = Objects.requireNonNull(storageProperties);
        this.storage = Objects.requireNonNull(storage);
    }

    @Override
    public void storeFile(String filePath, String contentType, byte[] content) {
        BlobId id = BlobId
                .of(storageProperties.getBucket(), Objects.requireNonNull(filePath));
        BlobInfo blobInfo = BlobInfo
                .newBuilder(id)
                .setContentType(contentType)
                .build();

        storage.create(blobInfo, content);
    }

    @Override
    public byte[] getFile(String id) {
        return new byte[0];
    }

    @Override
    public void deleteFile(String id) {

    }
}
