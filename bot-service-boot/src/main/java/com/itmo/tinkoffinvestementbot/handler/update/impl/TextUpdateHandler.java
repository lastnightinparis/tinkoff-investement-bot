package com.itmo.tinkoffinvestementbot.handler.update.impl;

import com.itmo.tinkoffinvestementbot.converter.UserConverter;
import com.itmo.tinkoffinvestementbot.exception.chat.BotUserDeniedException;
import com.itmo.tinkoffinvestementbot.exception.chat.ProcessingException;
import com.itmo.tinkoffinvestementbot.exception.system.IllegalTradingBotException;
import com.itmo.tinkoffinvestementbot.handler.registry.UpdateHandlersRegistry;
import com.itmo.tinkoffinvestementbot.handler.update.BotUpdateHandler;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import com.itmo.tinkoffinvestementbot.model.enums.bot.Button;
import com.itmo.tinkoffinvestementbot.model.enums.handler.UpdateType;
import com.itmo.tinkoffinvestementbot.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TextUpdateHandler implements BotUpdateHandler {

    private final UserService userService;
    private final UserConverter userConverter;
    private final UpdateHandlersRegistry updateHandlersRegistry;

    @Override
    public void handelUpdates(Update update) {
        Message message = update.getMessage();
        Long userChatId = message.getFrom().getId();
        User user;

        if (userService.isNew(userChatId)) {
            // Если пользователь новый
            if (message.getFrom().getIsBot()) {
                // Если пользователь является ботом
                throw new BotUserDeniedException(userChatId);
            } else {
                // Если новый пользователь не является ботом

                user = userService.save(
                        userConverter.fromTgApi(message.getFrom())
                );

                try {
                    updateHandlersRegistry
                            .getTextHandlerFor(BotState.FIRST_START)
                            .handleText(user, message);
                } catch (IllegalTradingBotException e) {
                    throw new ProcessingException(
                            userChatId,
                            "There is no text handler for bot state - " + BotState.FIRST_START,
                            e
                    );
                }
            }
        } else {
            // Если пользователь зарегистрирован в системе
            user = userService.getUserByChatId(userChatId);

            if (user.isBlocked()) {
                // Если пользователь разблокировал бота
                user.setBlocked(false);
                user = userService.save(user);
            }
            Optional<Button> buttonOpt = Button.findByMessage(message.getText());

            if (buttonOpt.isPresent()) {
                // Если состояние бота можно определить по кнопке
                user = userService.setCurrentBotState(
                        user.getChatId(), buttonOpt.get().getBotState()
                );
            }
            try {
                updateHandlersRegistry
                        .getTextHandlerFor(user.getBotState())
                        .handleText(user, message);
            } catch (IllegalTradingBotException e) {
                throw new ProcessingException(
                        userChatId,
                        "There is no text handler for bot state - " + user.getBotState(),
                        e
                );
            }
        }
    }

    @Override
    public UpdateType supportedUpdateType() {
        return UpdateType.TEXT;
    }
}
