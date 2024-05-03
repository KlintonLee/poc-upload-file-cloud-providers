package com.klinton.poc.store.objects.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.cloud.storage")
public class GoogleStorageProperties implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleStorageProperties.class);

    private String credentials;

    private String projectId;

    private String bucket;

    @Override
    public String toString() {
        return "GoogleStorageProperties{" +
                ", projectId='" + projectId + '\'' +
                ", bucket='" + bucket + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() {
        LOGGER.info(String.valueOf(this));
    }

    public String getCredentials() {
        return credentials;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getBucket() {
        return bucket;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
