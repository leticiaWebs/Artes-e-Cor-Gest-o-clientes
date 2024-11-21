package com.artesecor.api_gestaoclientes.delivery.handler;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
