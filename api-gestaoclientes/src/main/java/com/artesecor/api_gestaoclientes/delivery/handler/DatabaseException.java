package com.artesecor.api_gestaoclientes.delivery.handler;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String msg){
        super(msg);
    }
}
