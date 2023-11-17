package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.service.bot.BotSenderService;
import com.itmo.tinkoffinvestementbot.service.users.UserService;
import jakarta.ws.rs.Consumes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tinkoffinvestementbot.dto.bot.NotificationDto;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/event", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class EventController {

    private final UserService userService;
    private final BotSenderService senderService;

    @PostMapping(path = "/trading")
    public ResponseEntity<Object> handelEvent(@RequestBody NotificationDto notificationDto) {
        User user = userService.getUserById(notificationDto.userId());

        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(user.getChatId()))
                .parseMode(ParseMode.MARKDOWNV2)
                .text(notificationDto.message())
                .build();

        senderService.sendMessage(sendMessage);

        return ResponseEntity.noContent().build();
    }
}
