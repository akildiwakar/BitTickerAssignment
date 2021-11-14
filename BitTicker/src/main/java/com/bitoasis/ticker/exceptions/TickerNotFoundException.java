package com.bitoasis.ticker.exceptions;

public class TickerNotFoundException extends RuntimeException {
    public TickerNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public TickerNotFoundException(String exMessage) {
        super(exMessage);
    }
}
