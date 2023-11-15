package com.itmo.tinkoffinvestementbot.handler.scenario.account;

import com.itmo.tinkoffinvestementbot.handler.registry.ResourceMessageRegistry;
import com.itmo.tinkoffinvestementbot.handler.type.BotStateTypeHandler;
import com.itmo.tinkoffinvestementbot.handler.update.CallbackUpdate;
import com.itmo.tinkoffinvestementbot.handler.update.TextUpdate;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import com.itmo.tinkoffinvestementbot.service.keyboard.InlineKeyboardService;
import com.itmo.tinkoffinvestementbot.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import tinkoffinvestementbot.dto.bot.ValidateTokenResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountManagementHandler implements CallbackUpdate, TextUpdate, BotStateTypeHandler {


    private final UserService userService;
    private final BotSenderService senderService;
    private final ResourceMessageRegistry messageRegistry;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public void handleCallback(User user, CallbackQuery callbackQuery) {
        EditMessageText editMessageText = EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(String.valueOf(user.getChatId())).text("")
                .parseMode(MARKDOWN)
                .build();

        String[] args = callbackQuery.getData().split("_");
        String action = args[1];

        switch (user.getBotState()) {
            case INVEST_ACCOUNT_ACTION:
                switch (action) {
                    case "connect" -> {
                        userService.setCurrentBotState(user.getChatId(), BotState.ENTER_TOKEN);
                        editMessageText.setText(messageRegistry.getMessage("account.enterToken"));
                    }
                    case "disconnect" -> {
                        // TODO: отправить запрос на удаление токена
                        editMessageText.setText(messageRegistry.getMessage("account.disconnect"));
                        user.setConnectedInvestAccount(false);
                        userService.save(user);
                    }
                    default -> log.warn("Unknown action - " + action);
                }
                break;
            default:
                log.warn("Unknown bot state - {}", user.getBotState());
        }
        senderService.sendEditMessageTextAsync(editMessageText);
    }

    @Override
    public void handleText(User user, Message message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(user.getChatId()))
                .parseMode(ParseMode.MARKDOWN)
                .text("")
                .build();
        switch (user.getBotState()) {
            case ACCOUNT_SETTINGS -> {
                String messageText = String.format(
                        messageRegistry.getMessage("account.info"),
                        user.getUsername(), user.getChatId(),
                        user.isConnectedInvestAccount() ? "Подключен" : "Не подключен"
                );

                Map<String, Boolean> conditions = new HashMap<>();
                conditions.put("isConnectedAccount", user.isConnectedInvestAccount());
                sendMessage.setText(messageText);
                sendMessage.setReplyMarkup(
                        inlineKeyboardService.getDynamicInlineKeyboard("ACCOUNT_INFO")
                                .generateDynamicConditionsKeyboard(conditions)
                );
            }
            case ENTER_TOKEN -> {
                sendMessage.setText(messageRegistry.getMessage("account.processing"));
                String apiToken = message.getText();
                // TODO: Отправить токен через CompletableFuture и получить ValidateTokenResponse
                CompletableFuture.completedFuture(new ValidateTokenResponse("", true))
                        .thenApply(response -> {
                            SendMessage newMessage = new SendMessage();
                            BeanUtils.copyProperties(sendMessage, newMessage);
                            String messageText;
                            if (response.isSuccess()) {
                                messageText = messageRegistry.getMessage("account.success");
                                user.setConnectedInvestAccount(true);
                                userService.save(user);
                            } else {
                                messageText = messageRegistry.getMessage("account.fail");
                            }
                            newMessage.setText(messageText);
                            return senderService.sendMessage(newMessage);
                        }).join();
                // Тут мб напутал)
            }
            default -> log.warn("Unknown bot state - {}", user.getBotState());
        }
        senderService.sendMessageAsync(sendMessage, 0);
    }


    @Override
    public List<BotState> getSupportedBotStates() {
        return List.of(
                BotState.ACCOUNT_SETTINGS, BotState.ENTER_TOKEN,
                BotState.INVEST_ACCOUNT_ACTION
        );
    }
}
