package com.itmo.tinkoffinvestementbot.facade;

import com.itmo.tinkoffinvestementbot.exception.system.IllegalTradingBotException;
import com.itmo.tinkoffinvestementbot.handler.registry.UpdateTypeHandlerRegistry;
import com.itmo.tinkoffinvestementbot.model.enums.handler.UpdateType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramFacade {

    private final UpdateTypeHandlerRegistry registry;

    public BotApiMethod<?> updateHandler(Update update) {
        Long chatId;
        try {
            if (update.hasMessage()) {
                return processMessageUpdate(update);
            } else if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                chatId = callbackQuery.getMessage().getChatId();
                log.info("Handle CallbackQuery from chatId - {} and callback - {}",
                        chatId, callbackQuery.getData());
                registry.getUpdateHandlerFor(UpdateType.CALLBACK)
                        .handelUpdates(update);
            } else {
                return null;
            }
            return SendChatAction.builder()
                    .chatId(String.valueOf(chatId))
                    .action(ActionType.TYPING.toString())
                    .build();
        } catch (IllegalTradingBotException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BotApiMethod<? extends Serializable> processMessageUpdate(Update update) {
        Message message = update.getMessage();

        if (message.getText() != null) {
            log.info("Handle MessageQuery with text from chatId - {} and text - {}"
                    , message.getFrom().getId(), message.getText());
            registry.getUpdateHandlerFor(UpdateType.TEXT)
                    .handelUpdates(update);
        } else {
            return SendMessage.builder()
                    .chatId(String.valueOf(message.getChatId()))
                    .text("Данный тип сообщения бот не поддерживает!")
                    .build();
        }
        return SendChatAction.builder()
                .chatId(String.valueOf(message.getChatId()))
                .action(ActionType.TYPING.toString())
                .build();
    }
}
