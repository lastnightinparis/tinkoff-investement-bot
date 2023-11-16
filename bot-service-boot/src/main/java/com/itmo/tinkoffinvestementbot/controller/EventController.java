package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import com.itmo.tinkoffinvestementbot.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tinkoffinvestementbot.dto.bot.NotificationDto;

@RequiredArgsConstructor
@RestController("/event")
public class EventController {

    private final UserService userService;
    private final BotSenderService senderService;

    @GetMapping("/trading")
    public void handelEvent(NotificationDto notificationDto) {
        User user = userService.getUserById(notificationDto.userId());

        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(user.getChatId()))
                .parseMode(ParseMode.MARKDOWNV2)
                .text(notificationDto.message())
                .build();

        senderService.sendMessage(sendMessage);
    }
}
