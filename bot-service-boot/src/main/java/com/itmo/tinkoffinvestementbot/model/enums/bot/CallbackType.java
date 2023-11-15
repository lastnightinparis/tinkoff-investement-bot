package com.itmo.tinkoffinvestementbot.model.enums.bot;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum CallbackType {

    STRATEGY("strategy", BotState.START),
    EDIT_NOTIFICATION_EVENT("editNotificationEvent", BotState.START),
    CANCEL_NOTIFICATION_EVENT("cancelNotificationEvent", BotState.START),
    INVEST_ACCOUNT_ACTION("InvestAccount", BotState.INVEST_ACCOUNT_ACTION),
    CHOOSE_TYPE_OF_CHANGING("chooseTypeOfChange", BotState.START);

    private final String message;
    private final BotState botState;

    CallbackType(String message, BotState botState) {
        this.message = message;
        this.botState = botState;
    }

    public String getFullMessage(String prefix) {
        return message + "_" + prefix;
    }

    public static Optional<CallbackType> findByMessage(String message) {
        return Stream.of(CallbackType.values())
                .filter(button -> message.startsWith(button.message))
                .findFirst();
    }
}
