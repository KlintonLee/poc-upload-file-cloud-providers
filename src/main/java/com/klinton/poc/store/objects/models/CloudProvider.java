package com.klinton.poc.store.objects.models;

import java.util.Arrays;
import java.util.Optional;

public enum CloudProvider {
    GCP,
    AWS;

    public static Optional<CloudProvider> findByProvider(String providerName) {
        return Arrays.stream(values())
                .filter(provider -> provider.name().equalsIgnoreCase(providerName))
                .findFirst();
    }
}
