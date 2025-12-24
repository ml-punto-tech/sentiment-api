package com.one8.sentiment_tech_api.exceptions;

public class ServiceUnavailableException  extends RuntimeException{


    public ServiceUnavailableException(String message){
        super(message);
    }
}
