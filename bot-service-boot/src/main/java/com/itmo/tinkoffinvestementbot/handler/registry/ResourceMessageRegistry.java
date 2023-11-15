package com.itmo.tinkoffinvestementbot.handler.registry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Обработчик сообщений в соответствие с выбранной логалью.
 */
@Component
public class ResourceMessageRegistry {

    private final Locale locale;

    private final MessageSource messageSource;

    public ResourceMessageRegistry(
            @Value("${app.localeTag}") Locale locale,
            MessageSource messageSource
    ) {
        this.locale = locale;
        this.messageSource = messageSource;
    }

    /**
     * Метод возвращающий сообщение для выбранной локали.
     *
     * @param message Тип сообщения
     * @return Сообщение
     */
    public String getMessage(String message) {
        return messageSource.getMessage(message, null, locale);
    }

    /**
     * Метод возвращающий сообщение.
     *
     * @param message      Тип сообщения
     * @param localeString Локаль
     * @return Сообщение
     */
    public String getMessage(String message, String localeString) {
        return messageSource.getMessage(message, null, Locale.forLanguageTag(localeString));
    }

    /**
     * Метод возвращающий сообщение.
     *
     * @param message Тип сообщения
     * @param args    Дополнительные аргументы
     * @return Сообщение
     */
    public String getMessage(String message, Object... args) {
        return messageSource.getMessage(message, args, locale);
    }

}
