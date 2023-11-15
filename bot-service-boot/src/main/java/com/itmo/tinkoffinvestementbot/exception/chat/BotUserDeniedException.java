package com.itmo.tinkoffinvestementbot.exception.chat;

import com.itmo.tinkoffinvestementbot.exception.ChatException;

public class BotUserDeniedException extends ChatException {

    public BotUserDeniedException(Long chatId) {
        super(chatId);
    }

}
