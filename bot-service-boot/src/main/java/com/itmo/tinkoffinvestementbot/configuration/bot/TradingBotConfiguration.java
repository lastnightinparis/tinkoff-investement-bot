package com.itmo.tinkoffinvestementbot.configuration.bot;

import com.itmo.tinkoffinvestementbot.model.bot.TradingBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class TradingBotConfiguration {

    private final BotProperties botConfig;
    private final RestTemplate restTemplate;

    /**
     * Bean для создания springWebhookBot.
     *
     * @return TelegramBot
     */
    @Bean
    public TradingBot tradingBot() {
        return new TradingBot(botConfig);
    }
}
