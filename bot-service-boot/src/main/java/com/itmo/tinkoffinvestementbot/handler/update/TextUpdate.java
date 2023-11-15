package com.itmo.tinkoffinvestementbot.handler.update;

import com.itmo.tinkoffinvestementbot.model.domain.User;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TextUpdate {

    void handleText(User user, Message message);
}
