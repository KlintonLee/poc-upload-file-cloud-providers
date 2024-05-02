package com.klinton.poc.store.objects.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        // No StackTrace for this exception
        super(message, null, true, false);
    }
}
