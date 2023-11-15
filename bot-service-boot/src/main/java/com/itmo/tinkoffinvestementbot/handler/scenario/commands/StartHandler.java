package com.itmo.tinkoffinvestementbot.handler.scenario.commands;

import com.itmo.tinkoffinvestementbot.handler.registry.ResourceMessageRegistry;
import com.itmo.tinkoffinvestementbot.handler.type.BotStateTypeHandler;
import com.itmo.tinkoffinvestementbot.handler.update.TextUpdate;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import com.itmo.tinkoffinvestementbot.service.keyboard.ReplyKeyboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartHandler implements TextUpdate, BotStateTypeHandler {

    private final BotSenderService senderService;
    private final ResourceMessageRegistry messageRegistry;
    private final ReplyKeyboardService replyKeyboardService;

    @Override
    public void handleText(User user, Message message) {
        SendMessage sendMessage = SendMessage.builder()
                .parseMode(ParseMode.MARKDOWN)
                .chatId(String.valueOf(message.getChatId()))
                .text("")
                .build();

        switch (user.getBotState()) {
            case START -> sendMessage.setText(String.format(
                    messageRegistry.getMessage("start.user"),
                    user.getUsername(), user.getChatId()
            ));
            case FIRST_START -> {
                String message1 = messageRegistry.getMessage("start.newUser");
                System.out.println(message1);
                String format = String.format(
                        message1,
                        user.getUsername(), user.getChatId()
                );
                sendMessage.setText(format);
                break;
            }
            default -> log.warn("Unknown bot state - {}", user.getBotState());
        }
        sendMessage.setReplyMarkup(
                replyKeyboardService.getStaticKeyboardMarkup("MAIN_MENU")
        );
        senderService.sendMessageAsync(sendMessage, 0);
    }

    @Override
    public List<BotState> getSupportedBotStates() {
        return List.of(
                BotState.START, BotState.FIRST_START
        );
    }
}
