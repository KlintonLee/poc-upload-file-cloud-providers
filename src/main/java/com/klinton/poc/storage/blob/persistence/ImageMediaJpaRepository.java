package com.klinton.poc.storage.blob.persistence;

import com.klinton.poc.storage.blob.models.ImageMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageMediaJpaRepository extends JpaRepository<ImageMedia, String> {
}
