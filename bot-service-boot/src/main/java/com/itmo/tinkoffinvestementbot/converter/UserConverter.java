package com.itmo.tinkoffinvestementbot.converter;

import com.itmo.tinkoffinvestementbot.model.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {


    public User fromTgApi(org.telegram.telegrambots.meta.api.objects.User user) {
        Long chatId = user.getId();
        String username;
        String tgUsername;

        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            username = user.getUserName();
            tgUsername = user.getUserName();
        } else if (!user.getFirstName().isEmpty()) {
            username = user.getFirstName();
            tgUsername = "user_" + chatId;
        } else {
            username = "user_" + chatId;
            tgUsername = "user_" + chatId;
        }
        return User.builder()
                .chatId(chatId)
                .username(username)
                .tgUsername(tgUsername)
                .build();
    }
}
