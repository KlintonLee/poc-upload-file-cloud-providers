package com.klinton.poc.store.objects.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.Duration;
import java.util.Objects;

@Configuration
public class S3StorageConfig {

    private final HttpProperties httpProps;

    private final S3StorageProperties awsProps;

    public S3StorageConfig(
            final HttpProperties httpProps,
            final S3StorageProperties awsProps
    ) {
        this.httpProps = Objects.requireNonNull(httpProps);
        this.awsProps = Objects.requireNonNull(awsProps);
    }

    public AwsCredentialsProvider awsCredentials() {
        AwsCredentials credentials = AwsBasicCredentials.create(awsProps.getAccessKey(), awsProps.getSecretKey());
        return StaticCredentialsProvider.create(credentials);
    }

    @Bean
    public S3Client s3Client() {
        SdkHttpClient apacheHttpClient = ApacheHttpClient.builder()
                .connectionTimeout(Duration.ofMillis(httpProps.getConnectTimeout()))
                .socketTimeout(Duration.ofMillis(httpProps.getReadTimeout()))
                .build();

        return S3Client.builder()
                .region(Region.of(awsProps.getRegion()))
                .httpClient(apacheHttpClient)
                .credentialsProvider(awsCredentials())
                .build();
    }
}
