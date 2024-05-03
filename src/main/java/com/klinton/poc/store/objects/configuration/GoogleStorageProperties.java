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

    private int connectTimeout;

    private int readTimeout;

    private int retryDelay;

    private int retryMaxDelay;

    private int retryMaxAttempts;

    private double retryMultiplier;

    @Override
    public String toString() {
        return "GoogleStorageConfig{" +
                ", projectId='" + projectId + '\'' +
                ", bucket='" + bucket + '\'' +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", retryDelay=" + retryDelay +
                ", retryMaxDelay=" + retryMaxDelay +
                ", retryMaxAttempts=" + retryMaxAttempts +
                ", retryMultiplier=" + retryMultiplier +
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

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public int getRetryMaxDelay() {
        return retryMaxDelay;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public double getRetryMultiplier() {
        return retryMultiplier;
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

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
    }

    public void setRetryMaxDelay(int retryMaxDelay) {
        this.retryMaxDelay = retryMaxDelay;
    }

    public void setRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public void setRetryMultiplier(double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
    }
}
