package com.itmo.tinkoffinvestementbot.configuration.bot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Telegram бота.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.bot")
public class BotProperties {

    /**
     * Ник бота.
     */
    private String name;

    /**
     * Токен.
     */
    private String token;

    /**
     * Путь для отправки WebHook.
     */
    private String webHookPath;

}
