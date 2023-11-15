package com.itmo.tinkoffinvestementbot.handler.update;

import com.itmo.tinkoffinvestementbot.model.enums.handler.UpdateType;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotUpdateHandler {

    void handelUpdates(Update update);

    UpdateType supportedUpdateType();
}
