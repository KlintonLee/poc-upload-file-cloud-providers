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

@Configuration
public class GoogleStorageConfig {

    private final GoogleStorageProperties props;

    public GoogleStorageConfig(GoogleStorageProperties googleStorageProperties) {
        this.props = googleStorageProperties;
    }

    @Bean
    public Credentials credentials() throws IOException {
        final var jsonContent = Base64.decodeBase64(props.getCredentials());

        return GoogleCredentials.fromStream(new ByteArrayInputStream(jsonContent));
    }

    @Bean
    public Storage storage(
            final Credentials credentials
    ) {
        final var transportOptions = HttpTransportOptions.newBuilder()
                .setConnectTimeout(props.getConnectTimeout())
                .setReadTimeout(props.getReadTimeout())
                .build();

        final var retrySettings = RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(props.getRetryDelay()))
                .setMaxAttempts(props.getRetryMaxAttempts())
                .setMaxRetryDelay(Duration.ofMillis(props.getRetryMaxDelay()))
                .setRetryDelayMultiplier(props.getRetryMultiplier())
                .build();

        final var options = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setRetrySettings(retrySettings)
                .setTransportOptions(transportOptions)
                .build();

        return options.getService();
    }
}
