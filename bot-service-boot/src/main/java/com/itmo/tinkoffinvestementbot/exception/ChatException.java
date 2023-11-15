package com.itmo.tinkoffinvestementbot.exception;

public class ChatException extends TradingBotException {

    private Long chatId;

    public ChatException(Long chatId) {
        this.chatId = chatId;
    }

    public ChatException(Long chatId, String message) {
        super(message);
        this.chatId = chatId;
    }

    public ChatException(Long chatId, String message, Throwable cause) {
        super(message, cause);
        this.chatId = chatId;
    }

    public ChatException(Long chatId, Throwable cause) {
        super(cause);
        this.chatId = chatId;
    }

    public ChatException(
            Long chatId, String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }
}
