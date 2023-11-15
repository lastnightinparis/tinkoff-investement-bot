package com.itmo.tinkoffinvestementbot.handler.type;

import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;

import java.util.List;

public interface BotStateTypeHandler {

    List<BotState> getSupportedBotStates();
}
