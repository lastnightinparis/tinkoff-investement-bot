package com.itmo.tinkoffinvestementbot.handler.update;

import com.itmo.tinkoffinvestementbot.model.domain.User;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackUpdate {

    void handleCallback(User user, CallbackQuery callbackQuery);
}
