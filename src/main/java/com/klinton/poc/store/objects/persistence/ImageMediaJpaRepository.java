package com.klinton.poc.store.objects.persistence;

import com.klinton.poc.store.objects.models.ImageMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageMediaJpaRepository extends JpaRepository<ImageMedia, String> {
}
