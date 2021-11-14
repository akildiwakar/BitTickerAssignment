package com.bitoasis.ticker.exceptions;

import org.springframework.http.HttpStatus;

public class TickerApiException  extends RuntimeException {

    private HttpStatus status;
    private String message;

    public TickerApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public TickerApiException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}