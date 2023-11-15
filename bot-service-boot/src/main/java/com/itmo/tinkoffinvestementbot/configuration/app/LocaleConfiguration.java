package com.itmo.tinkoffinvestementbot.configuration.app;

import jakarta.annotation.PostConstruct;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Конфигурация локали.
 */
@Configuration
public class LocaleConfiguration {

    /**
     * Инициализация временной зоны.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
        Locale.setDefault(new Locale("ru"));
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
