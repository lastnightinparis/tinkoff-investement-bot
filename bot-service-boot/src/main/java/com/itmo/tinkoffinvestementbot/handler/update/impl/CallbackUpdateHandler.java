package com.itmo.tinkoffinvestementbot.handler.update.impl;

import com.itmo.tinkoffinvestementbot.exception.chat.BotUserDeniedException;
import com.itmo.tinkoffinvestementbot.exception.chat.ProcessingException;
import com.itmo.tinkoffinvestementbot.exception.chat.UnknownUserException;
import com.itmo.tinkoffinvestementbot.exception.system.IllegalTradingBotException;
import com.itmo.tinkoffinvestementbot.handler.registry.UpdateHandlersRegistry;
import com.itmo.tinkoffinvestementbot.handler.update.BotUpdateHandler;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.enums.bot.CallbackType;
import com.itmo.tinkoffinvestementbot.model.enums.handler.UpdateType;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import com.itmo.tinkoffinvestementbot.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallbackUpdateHandler implements BotUpdateHandler {

    private final UserService userService;
    private final BotSenderService senderService;
    private final UpdateHandlersRegistry updateHandlersRegistry;

    @Override
    public void handelUpdates(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long userChatId = callbackQuery.getFrom().getId();
        senderService.sendAnswerCallbackQuery(
                AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .build()
        );
        User user;

        if (userService.isNew(userChatId)) {
            // Если пользователь новый
            if (callbackQuery.getFrom().getIsBot()) {
                // Если пользователь является ботом
                throw new BotUserDeniedException(userChatId);
            } else {
                // Если новый пользователь не является ботом
                throw new UnknownUserException(userChatId);
            }
        } else {
            // Если пользователь зарегистрирован в системе
            user = userService.getUserByChatId(userChatId);
            Optional<CallbackType> callbackTypeOpt =
                    CallbackType.findByMessage(callbackQuery.getData());

            if (callbackTypeOpt.isPresent()) {
                // Если состояние бота можно определить по кнопке
                user = userService.setCurrentBotState(
                        user.getChatId(), callbackTypeOpt.get().getBotState()
                );
                try {
                    updateHandlersRegistry
                            .getCallbackHandlerFor(user.getBotState())
                            .handleCallback(user, callbackQuery);
                } catch (IllegalTradingBotException e) {
                    throw new ProcessingException(
                            userChatId,
                            "There is no callback handler for bot state - " + user.getBotState(),
                            e
                    );
                }
            } else {
                throw new ProcessingException(
                        userChatId,
                        "There is no callback handler for callback - " + callbackQuery.getData()
                );
            }
        }
    }

    @Override
    public UpdateType supportedUpdateType() {
        return UpdateType.CALLBACK;
    }
}
