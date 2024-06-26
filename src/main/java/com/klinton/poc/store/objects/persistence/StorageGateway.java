package com.klinton.poc.store.objects.persistence;

public interface StorageGateway {

    void storeFile(String filePath, String contentType, byte[] content);

    byte[] getFileBytes(String id);

    void deleteFile(String id);
}
