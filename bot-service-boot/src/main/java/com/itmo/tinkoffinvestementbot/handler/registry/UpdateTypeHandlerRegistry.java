package com.itmo.tinkoffinvestementbot.handler.registry;

import com.itmo.tinkoffinvestementbot.exception.system.IllegalTradingBotException;
import com.itmo.tinkoffinvestementbot.handler.update.BotUpdateHandler;
import com.itmo.tinkoffinvestementbot.model.enums.handler.UpdateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateTypeHandlerRegistry {

    private final List<BotUpdateHandler> updateHandlers;

    public BotUpdateHandler getUpdateHandlerFor(UpdateType updateType) {
        return updateHandlers.stream()
                .filter(handler -> handler.supportedUpdateType().equals(updateType))
                .findAny()
                .orElseThrow(() -> new IllegalTradingBotException("Unknown update type: " + updateType));
    }
}
