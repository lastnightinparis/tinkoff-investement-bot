package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.exception.TradingBotException;
import com.itmo.tinkoffinvestementbot.exception.chat.*;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import com.itmo.tinkoffinvestementbot.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final UserService userService;
    private final BotSenderService senderService;

    private static final String PROCESSING_ERROR_MSG =
            "В процессе обработки вашего сообщения произошла ошибка😢\n"
                    + "Наша команда уже ознакомлена с ней и в скором времени исправит!👨🏻‍💻";
    private static final String BOT_USER_DENIED_MSG = "Ботам доступ запрещен!";
    private static final String UNKNOWN_UPDATE_TYPE_MSG = "Сейчас я не готов обработать это сообщение🥲";
    private static final String UNKNOWN_USER_MSG = "Вы еще не зарегистрированы в системе, отправьте боту `/start`🚀";
    private static final String ACCESS_DENIED_MSG = "Доступ запрещен";

    @ExceptionHandler(value = BotUserDeniedException.class)
    public void handleBotUserDeniedException(BotUserDeniedException e) {
        senderService.sendMessageAsync(
                SendMessage.builder()
                        .chatId(String.valueOf(e.getChatId()))
                        .text(BOT_USER_DENIED_MSG)
                        .build(), 0
        );
    }

    @ExceptionHandler(value = UnknownUserException.class)
    public void handleUnknownUserException(UnknownUserException e) {
        senderService.sendMessageAsync(
                SendMessage.builder()
                        .chatId(String.valueOf(e.getChatId()))
                        .text(UNKNOWN_USER_MSG)
                        .build(), 0
        );
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException e) {
        senderService.sendMessageAsync(
                SendMessage.builder()
                        .chatId(String.valueOf(e.getChatId()))
                        .text(ACCESS_DENIED_MSG)
                        .build(), 0
        );
    }

    @ExceptionHandler(value = BlockedUserException.class)
    public void handleBlockedUserException(BlockedUserException e) {
        log.info("User with id - {} was blocked bot", e.getChatId());
        User user = userService.getUserByChatId(e.getChatId());
        user.setBlocked(true);
        userService.save(user);
    }

    @ExceptionHandler(value = UnknownUpdateTypeException.class)
    public void handleUnknownUpdateTypeException(UnknownUpdateTypeException e) {
        senderService.sendMessageAsync(
                SendMessage.builder()
                        .chatId(String.valueOf(e.getChatId()))
                        .text(UNKNOWN_UPDATE_TYPE_MSG)
                        .build(), 0
        );
    }

    @ExceptionHandler(value = ProcessingException.class)
    public void handleProcessingException(ProcessingException e) {
        senderService.sendMessageAsync(
                SendMessage.builder()
                        .chatId(String.valueOf(e.getChatId()))
                        .text(PROCESSING_ERROR_MSG)
                        .build(), 0
        );
        senderService.notifyMasters(e.getMasterMessage());
    }

    @ExceptionHandler(value = TradingBotException.class)
    public void handleMonitorBotException(TradingBotException e) {
        log.warn("Exception: \n{}", e.getMessage());
    }
}
