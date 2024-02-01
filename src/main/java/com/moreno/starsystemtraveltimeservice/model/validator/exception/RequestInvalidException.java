package com.moreno.starsystemtraveltimeservice.model.validator.exception;

public class RequestInvalidException extends RuntimeException  {
    public RequestInvalidException(String msg) {
        super(msg);
    }
}
