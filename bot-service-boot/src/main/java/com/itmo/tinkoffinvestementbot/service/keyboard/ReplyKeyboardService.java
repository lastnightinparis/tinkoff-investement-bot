package com.itmo.tinkoffinvestementbot.service.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Map;

public interface ReplyKeyboardService {

    ReplyKeyboardMarkup getStaticKeyboardMarkup(String name);

    ReplyKeyboardMarkup getDynamicKeyboardMarkup(String name, Map<String, Boolean> conditions);
}
