package com.one8.sentiment_tech_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException  extends RuntimeException{


    public ServiceUnavailableException(String message){
        super(message);
    }
}
