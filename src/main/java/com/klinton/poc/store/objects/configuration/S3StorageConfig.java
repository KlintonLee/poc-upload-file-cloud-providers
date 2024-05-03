package com.klinton.poc.store.objects.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Objects;

@Configuration
public class S3StorageConfig {

    private final S3StorageProperties props;

    public S3StorageConfig(final S3StorageProperties props) {
        this.props = Objects.requireNonNull(props);
    }

    public AwsCredentialsProvider awsCredentials() {
        AwsCredentials credentials = AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey());
        return StaticCredentialsProvider.create(credentials);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(awsCredentials())
                .build();
    }
}
