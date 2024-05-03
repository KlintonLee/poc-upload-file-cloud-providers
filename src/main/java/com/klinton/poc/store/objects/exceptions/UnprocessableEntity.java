package com.klinton.poc.store.objects.exceptions;

public class UnprocessableEntity extends RuntimeException {

    public UnprocessableEntity(String message) {
        // No StackTrace for this exception
        super(message, null, true, false);
    }
}
