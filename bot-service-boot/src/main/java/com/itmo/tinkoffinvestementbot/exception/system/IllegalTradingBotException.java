package com.itmo.tinkoffinvestementbot.exception.system;

import com.itmo.tinkoffinvestementbot.exception.TradingBotException;

public class IllegalTradingBotException extends TradingBotException {

    public IllegalTradingBotException(String message) {
        super(message);
    }

    public IllegalTradingBotException(String message, Throwable cause) {
        super(message, cause);
    }
}
