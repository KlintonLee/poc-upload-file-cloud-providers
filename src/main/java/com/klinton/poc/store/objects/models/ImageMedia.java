package com.klinton.poc.store.objects.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "images")
@Entity(name = "ImageMedia")
public class ImageMedia {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name="content_type", nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String location;

    public ImageMedia() {
    }

    private ImageMedia(String id, String name, String contentType, String location) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
        this.location = location;
    }

    public static ImageMedia create(final String id, final String name, final String contentType, final String location) {
        return new ImageMedia(id, name, contentType, location);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public String getLocation() {
        return location;
    }
}
