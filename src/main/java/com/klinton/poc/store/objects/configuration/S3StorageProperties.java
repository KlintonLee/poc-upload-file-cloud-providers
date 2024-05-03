package com.klinton.poc.store.objects.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3")
public class S3StorageProperties implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3StorageProperties.class);

    private String region;

    private String bucket;

    private String accessKey;

    private String secretKey;

    @Override
    public void afterPropertiesSet() {
        LOGGER.info(String.valueOf(this));
    }

    @Override
    public String toString() {
        return "S3StorageConfig{" +
                "region='" + region + '\'' +
                ", bucket='" + bucket + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
