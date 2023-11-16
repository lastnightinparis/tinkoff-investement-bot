package com.itmo.tinkoffinvestementbot.handler.scenario.trading;

import com.itmo.tinkoffinvestementbot.handler.registry.ResourceMessageRegistry;
import com.itmo.tinkoffinvestementbot.handler.type.BotStateTypeHandler;
import com.itmo.tinkoffinvestementbot.handler.update.CallbackUpdate;
import com.itmo.tinkoffinvestementbot.handler.update.TextUpdate;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.dto.trading.TempTradingOrderDto;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import com.itmo.tinkoffinvestementbot.service.keyboard.InlineKeyboardService;
import com.itmo.tinkoffinvestementbot.service.strategy.StrategyService;
import com.itmo.tinkoffinvestementbot.service.trading.TempTradingOrderService;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import tinkoffinvestementbot.dto.bot.ResponseStrategyInfoDto;
import tinkoffinvestementbot.dto.bot.ValidateTokenResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartTradingHandler implements CallbackUpdate, TextUpdate, BotStateTypeHandler {

    private final UserService userService;
    private final StrategyService strategyService;
    private final BotSenderService senderService;
    private final ResourceMessageRegistry messageRegistry;
    private final InlineKeyboardService inlineKeyboardService;
    private final TempTradingOrderService tempTradingOrderService;

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
            case CHOOSE_STRATEGY_ACTION -> {
                Long strategyId = Long.parseLong(action);
                HashMap<String, String> additionalParams = strategyService.getAdditionalParamsByStrategyId(strategyId);
                tempTradingOrderService.create(user.getId(), strategyId, additionalParams);
                editMessageText.setText(messageRegistry.getMessage("trading.enterTicket"));
                userService.setCurrentBotState(user.getChatId(), BotState.ENTER_TRADING_TICKER);
            }
            case CHOOSE_STRATEGY_PARAM -> {
                log.info(action);
                editMessageText.setText("Введите значение");
                // Сохраняем ключ запрашиваемого значения
                TempTradingOrderDto order = tempTradingOrderService.get(user.getId());
                order.setLastAdditionalParam(action);

                userService.setCurrentBotState(user.getChatId(), BotState.ENTER_ADDITIONAL_PARAM);
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
            case START_TRADING -> {
                sendMessage.setText(messageRegistry.getMessage("trading.chooseStrategy"));
                List<ResponseStrategyInfoDto> strategyInfoDtos = strategyService.getAllInfo();
                sendMessage.setReplyMarkup(
                        inlineKeyboardService.getDynamicFromResourceKeyboardMarkup(
                                "STRATEGIES_LIST",
                                getStrategyButtons(strategyInfoDtos)
                        )
                );
            }
            case ENTER_TRADING_TICKER -> {
                String ticker = message.getText();
                TempTradingOrderDto order = tempTradingOrderService.get(user.getId());
                order.setTicker(ticker);
                sendMessage.setText("Заполните все данные");
                sendMessage.setReplyMarkup(
                        getParamButtons(
                                order.getAdditionalParamNames(), order.getAdditionalParamValues()
                        )
                );
            }
            case ENTER_ADDITIONAL_PARAM -> {
                TempTradingOrderDto order = tempTradingOrderService.get(user.getId());
                order.addLastUsedParamValue(message.getText().trim().toLowerCase());

                boolean hasEmptyParam = order.getAdditionalParamNames().keySet()
                        .stream().anyMatch(key -> order.getAdditionalParamValues().get(key) == null);

                if (hasEmptyParam) {
                    sendMessage.setText("Заполните все данные");
                    sendMessage.setReplyMarkup(getParamButtons(
                            order.getAdditionalParamNames(), order.getAdditionalParamValues()
                    ));
                } else {
                    // TODO: Отправить заявку на активацию стратегии
                    ValidateTokenResponse response = new ValidateTokenResponse(true);
                    if (response.isSuccess()) {
                        sendMessage.setText(messageRegistry.getMessage("trading.successCreatingOrder"));
                    } else {
                        sendMessage.setText(messageRegistry.getMessage("trading.failCreatingOrder"));
                    }
                }
            }
            default -> log.warn("Unknown bot state - {}", user.getBotState());
        }
        senderService.sendMessageAsync(sendMessage, 0);
    }

    private InlineKeyboardMarkup getParamButtons(
            Map<String, String> additionalParamNames,
            Map<String, String> additionalParamValues
    ) {
        return inlineKeyboardService.getDynamicFromResourceKeyboardMarkup(
                "STRATEGIES_PARAM_LIST",
                additionalParamNames.keySet()
                        .stream().map(key -> {
                            String name = additionalParamNames.get(key);
                            return new InlineButtonWrappedObject() {
                                @Override
                                public String getName() {
                                    return name + " " + (additionalParamValues.containsKey(key) ? "\u2705" : "\u274C");
                                }

                                @Override
                                public String getKey() {
                                    return key;
                                }
                            };
                        }).toList()
        );
    }

    private static List<? extends InlineButtonWrappedObject> getStrategyButtons(List<ResponseStrategyInfoDto> strategyInfoDtos) {
        return strategyInfoDtos
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
                BotState.START_TRADING, BotState.CHOOSE_STRATEGY_ACTION,
                BotState.ENTER_TRADING_TICKER, BotState.CHOOSE_STRATEGY_PARAM,
                BotState.ENTER_ADDITIONAL_PARAM
        );
    }
}
