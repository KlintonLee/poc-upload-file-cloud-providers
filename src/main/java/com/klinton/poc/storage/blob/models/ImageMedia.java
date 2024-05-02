package com.klinton.poc.storage.blob.models;

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

    @Column(nullable = false)
    private String location;

    public ImageMedia() {
    }

    private ImageMedia(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static ImageMedia create(final String id, final String name, final String location) {
        return new ImageMedia(id, name, location);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
