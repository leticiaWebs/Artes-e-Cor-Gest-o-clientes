package com.artesecor.api_gestaoclientes.delivery.handler;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
