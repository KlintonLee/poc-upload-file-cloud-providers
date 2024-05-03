package com.klinton.poc.store.objects.persistence;

import com.klinton.poc.store.objects.exceptions.UnprocessableEntity;
import com.klinton.poc.store.objects.models.CloudProvider;
import org.springframework.stereotype.Component;

@Component
public class StorageContext {

    private final S3StorageGatewayImpl s3StorageGateway;

    private final GoogleStorageGatewayImpl googleStorageGateway;

    public StorageContext(final S3StorageGatewayImpl s3StorageGateway, final GoogleStorageGatewayImpl googleStorageGateway) {
        this.s3StorageGateway = s3StorageGateway;
        this.googleStorageGateway = googleStorageGateway;
    }

    public StorageGateway getStorageGateway(final CloudProvider provider) {
        if (provider.equals(CloudProvider.AWS)) {
            return s3StorageGateway;
        } else if (provider.equals(CloudProvider.GCP)) {
            return googleStorageGateway;
        } else {
            throw new UnprocessableEntity("Invalid provider");
        }
    }

}
