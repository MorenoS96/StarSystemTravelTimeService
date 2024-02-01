package com.moreno.starsystemtraveltimeservice.model.validator.exception;
//to give messages to the end user. so that he can adjust his request.
//these exceptions are expected to happen. mainly thrown by the validators
public class RequestInvalidException extends RuntimeException  {
    public RequestInvalidException(String msg) {
        super(msg);
    }
}
