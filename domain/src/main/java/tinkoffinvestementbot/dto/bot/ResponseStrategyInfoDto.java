package tinkoffinvestementbot.dto.bot;

import java.util.HashMap;

/**
 * @param strategyId ID Стратегии
 * @param name Название, которое будет выводиться на кнопки
 * @param additionalParams Дополнительные параметры <название на английском, название на русском>
 * Пример: { "Супер стратегия", { "currency": "валюта" } }
 */
public record ResponseStrategyInfoDto(
        Long strategyId,
        String name,
    HashMap<String, String> additionalParams
) {}
