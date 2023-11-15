package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.facade.TelegramFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class BotMessagesController {

    private final TelegramFacade telegramFacade;

    @PostMapping
    public PartialBotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramFacade.updateHandler(update);
    }
}
