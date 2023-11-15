package com.itmo.tinkoffinvestementbot.model.enums.bot;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum Button {
    // inline команды
    START("/start", BotState.START),
    BOT_VERSION_COMMAND("/bot_version", BotState.BOT_VERSION_COMMAND),
    HELP_COMMAND("/help", BotState.HELP_COMMAND),
    HELP("Помощь", BotState.HELP),

    TRADING_MENU("Торги", BotState.TRADING_MENU),
    NOTIFICATION_MENU("Уведомления", BotState.NOTIFICATION_MENU),
    ACCOUNT_SETTINGS("Аккаунт", BotState.ACCOUNT_SETTINGS),
    BACK("Назад", BotState.BACK),

    START_TRADING("Начать торги", BotState.START_TRADING),
    TRADINGS_LIST("Список активных стратегий", BotState.TRADINGS_LIST),
    STRATEGIES_LIST("Список стратегий", BotState.STRATEGIES_LIST),


    CREATE_NOTIFICATION_EVENT("Подключить уведомление", BotState.CREATE_NOTIFICATION_EVENT),
    NOTIFICATION_EVENT_LIST("Список уведомлений", BotState.NOTIFICATION_EVENT_LIST);

    private final String message;
    private final BotState botState;

    Button(String message, BotState botState) {
        this.message = message;
        this.botState = botState;
    }

    public static Optional<Button> findByMessage(String message) {
        return Stream.of(Button.values())
                .filter(button -> button.message.equals(message))
                .findFirst();
    }
}
