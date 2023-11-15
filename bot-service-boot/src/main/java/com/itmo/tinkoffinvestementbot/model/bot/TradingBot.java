package com.itmo.tinkoffinvestementbot.model.bot;

import com.itmo.tinkoffinvestementbot.configuration.bot.BotProperties;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TradingBot extends TelegramWebhookBot {
    private final String name;
    private final String token;
    private final String webhookPath;

    public TradingBot(
            BotProperties botConfig
    ) {
        super(new DefaultBotOptions());
        this.name = botConfig.getName();
        this.token = botConfig.getToken();
        this.webhookPath = botConfig.getWebHookPath();
    }

    public TradingBot(
            BotProperties botConfig,
            RestTemplate restTemplate
    ) {
        this(new DefaultBotOptions(), botConfig, restTemplate);
    }

    public TradingBot(
            DefaultBotOptions options, BotProperties botConfig,
            RestTemplate restTemplate
    ) {
        super(options);
        this.name = botConfig.getName();
        this.token = botConfig.getToken();
        this.webhookPath = botConfig.getWebHookPath();

        restTemplate.getForEntity(getBotUri(), String.class);
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public String getBotPath() {
        return this.webhookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    private String getBotUri() {
        String urlTemplate = "https://api.telegram.org/bot%s/setWebhook?url=%s";
        return String.format(urlTemplate, this.token, this.webhookPath);
    }
}
