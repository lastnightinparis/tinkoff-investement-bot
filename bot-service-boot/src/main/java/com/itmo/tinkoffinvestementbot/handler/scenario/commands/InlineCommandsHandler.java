package com.itmo.tinkoffinvestementbot.handler.scenario.commands;

import com.itmo.tinkoffinvestementbot.handler.registry.ResourceMessageRegistry;
import com.itmo.tinkoffinvestementbot.handler.type.BotStateTypeHandler;
import com.itmo.tinkoffinvestementbot.handler.update.TextUpdate;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InlineCommandsHandler implements TextUpdate, BotStateTypeHandler {

    @Value("${app.version}")
    private String version;

    private final BotSenderService senderService;
    private final ResourceMessageRegistry messageRegistry;

    @Override
    public void handleText(User user, Message message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(message.getChatId()))
                .parseMode(ParseMode.MARKDOWN)
                .text("")
                .build();

        switch (user.getBotState()) {
            case BOT_VERSION_COMMAND ->
                    sendMessage.setText(
                            String.format(messageRegistry.getMessage("command.botVersion"), version)
                    );
            case HELP_COMMAND, HELP ->
                    sendMessage.setText(messageRegistry.getMessage("command.faq"));
            default ->
                    log.warn("Unknown bot state - {}", user.getBotState());
        }

        senderService.sendMessageAsync(sendMessage,0);
    }

    @Override
    public List<BotState> getSupportedBotStates() {
        return List.of(
                BotState.BOT_VERSION_COMMAND,
                BotState.HELP_COMMAND, BotState.HELP
        );
    }
}