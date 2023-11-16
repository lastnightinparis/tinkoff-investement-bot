package tinkoffinvestementbot.dto.bot;

/**
 * @param strategyId ID Стратегии
 * @param name Название, которое будет выводиться на кнопки
 * @param description Описание стратегии
 */
public record ResponseStrategyWikiDto (
    Long strategyId, String name, String description
) {}
