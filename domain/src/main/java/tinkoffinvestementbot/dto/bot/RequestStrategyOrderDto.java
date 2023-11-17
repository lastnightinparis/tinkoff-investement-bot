package tinkoffinvestementbot.dto.bot;

import java.util.HashMap;

/**
 * @param userId ID пользователя
 * @param ticker Тикер акции
 * @param additionalParams Дополнительные параметры <название на английском, значение>
 * Пример: { 1, "APL", { "currency": "RUB" } }
 */
public record RequestStrategyOrderDto(
        Long userId,
        String ticker,
    HashMap<String, String> additionalParams
) {}
