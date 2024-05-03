package com.klinton.poc.store.objects.configuration;

import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

@Configuration
public class GoogleStorageConfig {

    private final HttpProperties httpProps;

    private final GoogleStorageProperties gcpProps;

    public GoogleStorageConfig(
            final HttpProperties httpProps,
            final GoogleStorageProperties googleStorageProperties
    ) {
        this.httpProps = Objects.requireNonNull(httpProps);
        this.gcpProps = Objects.requireNonNull(googleStorageProperties);
    }

    @Bean
    public Credentials credentials() throws IOException {
        final var jsonContent = Base64.decodeBase64(gcpProps.getCredentials());

        return GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent));
    }

    @Bean
    public Storage storage(
            final Credentials credentials
    ) {
        final var transportOptions = HttpTransportOptions.newBuilder()
                .setConnectTimeout(httpProps.getConnectTimeout())
                .setReadTimeout(httpProps.getReadTimeout())
                .build();

        final var retrySettings = RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(httpProps.getRetryDelay()))
                .setMaxAttempts(httpProps.getRetryMaxAttempts())
                .setMaxRetryDelay(Duration.ofMillis(httpProps.getRetryMaxDelay()))
                .setRetryDelayMultiplier(httpProps.getRetryMultiplier())
                .build();

        final var options = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setRetrySettings(retrySettings)
                .setTransportOptions(transportOptions)
                .build();

        return options.getService();
    }
}
