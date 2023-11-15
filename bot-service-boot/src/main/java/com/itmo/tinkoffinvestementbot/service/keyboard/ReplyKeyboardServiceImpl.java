package com.itmo.tinkoffinvestementbot.service.keyboard;

import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.reply.ReplyKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReplyKeyboardServiceImpl implements ReplyKeyboardService {

    @Qualifier(value = "staticReplyKeyboardMarkupMap")
    private final Map<String, ReplyKeyboardMarkup> staticReplyKeyboardMarkupMap;

    @Qualifier(value = "dynamicReplyKeyboard")
    private final Map<String, ReplyKeyboard> dynamicReplyKeyboardMap;

    @Override
    public ReplyKeyboardMarkup getStaticKeyboardMarkup(String name) {
        if (staticReplyKeyboardMarkupMap.containsKey(name)) {
            return staticReplyKeyboardMarkupMap.get(name);
        }
        throw new IllegalArgumentException("There is no static keyboard with name - " + name);
    }

    @Override
    public ReplyKeyboardMarkup getDynamicKeyboardMarkup(String name,
                                                        Map<String, Boolean> conditions) {
        if (dynamicReplyKeyboardMap.containsKey(name)) {
            return dynamicReplyKeyboardMap.get(name).generateDynamicKeyboard(conditions);
        }
        throw new IllegalArgumentException("There is no dynamic keyboard with name - " + name);
    }
}
