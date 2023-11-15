package com.itmo.tinkoffinvestementbot.handler.scenario.menu;

import com.itmo.tinkoffinvestementbot.handler.type.BotStateTypeHandler;
import com.itmo.tinkoffinvestementbot.handler.update.TextUpdate;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import com.itmo.tinkoffinvestementbot.service.keyboard.ReplyKeyboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuHandler implements TextUpdate, BotStateTypeHandler {

    private final BotSenderService senderService;
    private final ReplyKeyboardService replyKeyboardService;

    @Override
    public void handleText(User user, Message message) {
        log.info("Menu handler {}", user.getBotState());
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(user.getChatId()))
                .text("Меню обновлено")
                .build();

        switch (user.getBotState()) {
            case BACK:
                sendMessage.setReplyMarkup(replyKeyboardService
                        .getStaticKeyboardMarkup("MAIN_MENU"));
                break;
            case TRADING_MENU:
                sendMessage.setReplyMarkup(
                        replyKeyboardService.getStaticKeyboardMarkup("TRADING_MENU")
                );
                break;
            case NOTIFICATION_MENU:
                sendMessage.setReplyMarkup(
                            replyKeyboardService.getStaticKeyboardMarkup("EVENT_MENU")
                );
                break;
            default:
                log.warn("Unknown bot state - {}", user.getBotState());
        }

        senderService.sendMessageAsync(sendMessage, 0);
    }

    @Override
    public List<BotState> getSupportedBotStates() {
        return List.of(
                BotState.TRADING_MENU, BotState.NOTIFICATION_MENU,
                BotState.BACK
        );
    }
}
