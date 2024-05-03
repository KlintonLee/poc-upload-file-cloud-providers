package com.klinton.poc.store.objects.presenters;

public record ImageBase64(
        String contentType,
        String filename,
        String base64Data

) {

    public static ImageBase64 create(String contentType, String filename, String base64Data) {
        if (contentType == null || filename == null || base64Data == null) {
            throw new IllegalArgumentException("All fields are required for ImageBase64");
        }

        return new ImageBase64(contentType, filename, base64Data);
    }
}
