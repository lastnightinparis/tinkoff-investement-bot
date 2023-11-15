package com.itmo.tinkoffinvestementbot.configuration.handler;

import com.itmo.tinkoffinvestementbot.handler.update.CallbackUpdate;
import com.itmo.tinkoffinvestementbot.handler.update.TextUpdate;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import com.itmo.tinkoffinvestementbot.utils.StrategyUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class HandlersStrategyConfiguration {

    @Bean
    public Map<BotState, TextUpdate> textHandlers(List<TextUpdate> textHandlersList) {
        return StrategyUtils.botStateHandlersMap(textHandlersList);
    }

    @Bean
    public Map<BotState, CallbackUpdate> callbackHandlers(
            List<CallbackUpdate> callbackHandlersList
    ) {
        return StrategyUtils.botStateHandlersMap(callbackHandlersList);
    }
}
