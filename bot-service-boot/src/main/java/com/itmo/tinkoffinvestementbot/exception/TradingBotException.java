package com.itmo.tinkoffinvestementbot.exception;

public class TradingBotException extends RuntimeException {

    public TradingBotException() {
    }

    public TradingBotException(String message) {
        super(message);
    }

    public TradingBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public TradingBotException(Throwable cause) {
        super(cause);
    }

    public TradingBotException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
