package com.itmo.tinkoffinvestementbot.handler.registry;

import com.itmo.tinkoffinvestementbot.exception.system.IllegalTradingBotException;
import com.itmo.tinkoffinvestementbot.handler.update.CallbackUpdate;
import com.itmo.tinkoffinvestementbot.handler.update.TextUpdate;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UpdateHandlersRegistry {

    @Qualifier("textHandlers")
    private final Map<BotState, TextUpdate> textHandlers;
    @Qualifier("callbackHandlers")
    private final Map<BotState, CallbackUpdate> callbackHandlers;

    public TextUpdate getTextHandlerFor(BotState botState) {
        if (textHandlers.containsKey(botState)) {
            return textHandlers.get(botState);
        }
        throw new IllegalTradingBotException("Unknown text handler botState: " + botState.toString());
    }

    public CallbackUpdate getCallbackHandlerFor(BotState botState) {
        if (callbackHandlers.containsKey(botState)) {
            return callbackHandlers.get(botState);
        }
        throw new IllegalTradingBotException(
                "Unknown callback handler botState: " + botState.toString());
    }
}
