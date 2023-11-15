package com.itmo.tinkoffinvestementbot.utils;

import com.itmo.tinkoffinvestementbot.handler.type.BotStateTypeHandler;
import com.itmo.tinkoffinvestementbot.handler.type.StringTypeHandler;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StrategyUtils {

    private StrategyUtils() {
        throw new IllegalStateException("StrategyUtils is utility class");
    }

    public static <T> Map<BotState, T> botStateHandlersMap(
            List<T> botStateTypeHandlersList
    ) {
        return botStateTypeHandlersList.stream()
                .reduce(
                        new HashMap<>(),
                        (partialMap, update) -> {
                            HashMap<BotState, T> collect =
                                    ((BotStateTypeHandler) update).getSupportedBotStates()
                                            .stream()
                                            .collect(
                                                    Collectors.toMap(
                                                            botState -> botState, botState -> update,
                                                            (o1, o2) -> o1, HashMap::new
                                                    )
                                            );
                            partialMap.putAll(collect);
                            return partialMap;
                        },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }
                );
    }

    public static <T> Map<String, T> stringHandlersMap(
            List<T> stringTypeHandlersList
    ) {
        return stringTypeHandlersList.stream()
                .reduce(
                        new HashMap<>(),
                        (partialMap, update) -> {
                            HashMap<String, T> collect =
                                    ((StringTypeHandler) update).getSupportedTypes()
                                            .stream()
                                            .collect(
                                                    Collectors.toMap(
                                                            type -> type, type -> update,
                                                            (o1, o2) -> o1, HashMap::new
                                                    )
                                            );
                            partialMap.putAll(collect);
                            return partialMap;
                        },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        }
                );
    }
}