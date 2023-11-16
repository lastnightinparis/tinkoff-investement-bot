package com.itmo.tinkoffinvestementbot.handler.scenario.trading;

import com.itmo.tinkoffinvestementbot.handler.registry.ResourceMessageRegistry;
import com.itmo.tinkoffinvestementbot.handler.type.BotStateTypeHandler;
import com.itmo.tinkoffinvestementbot.handler.update.CallbackUpdate;
import com.itmo.tinkoffinvestementbot.handler.update.TextUpdate;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import com.itmo.tinkoffinvestementbot.service.keyboard.InlineKeyboardService;
import com.itmo.tinkoffinvestementbot.service.strategy.StrategyService;
import com.itmo.tinkoffinvestementbot.service.users.UserService;
import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline.InlineButtonWrappedObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import tinkoffinvestementbot.dto.bot.ResponseStrategyWikiDto;

import java.util.List;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyWikiListHandler implements CallbackUpdate, TextUpdate, BotStateTypeHandler {

    private final UserService userService;
    private final StrategyService strategyService;
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
            case STRATEGY_WIKI -> {
                Long strategyId = Long.parseLong(action);
                ResponseStrategyWikiDto wiki = strategyService.getWiki(strategyId);
                editMessageText.setText(wiki.name() + "\n\n" + wiki.description());
            }
            default -> log.warn("Unknown bot state - {}", user.getBotState());
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
            case STRATEGIES_LIST -> {
                List<ResponseStrategyWikiDto> allWiki = strategyService.getAllWiki();
                sendMessage.setText(messageRegistry.getMessage("trading.strategiesList"));
                sendMessage.setReplyMarkup(
                        inlineKeyboardService.getDynamicFromResourceKeyboardMarkup(
                                "STRATEGIES_WIKI_LIST",
                                getStrategyButtons(allWiki)
                        )
                );
            }
            default -> log.warn("Unknown bot state - {}", user.getBotState());
        }
        senderService.sendMessageAsync(sendMessage, 0);
    }

    private List<? extends InlineButtonWrappedObject> getStrategyButtons(List<ResponseStrategyWikiDto> allWiki) {
        return allWiki
                .stream().map(strategy -> new InlineButtonWrappedObject() {
                    @Override
                    public String getName() {
                        return strategy.name();
                    }

                    @Override
                    public String getKey() {
                        return strategy.strategyId().toString();
                    }
                }).toList();
    }


    @Override
    public List<BotState> getSupportedBotStates() {
        return List.of(
                BotState.STRATEGIES_LIST, BotState.STRATEGY_WIKI
        );
    }
}
