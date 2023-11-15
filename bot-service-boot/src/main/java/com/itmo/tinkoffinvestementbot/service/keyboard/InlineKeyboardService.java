package com.itmo.tinkoffinvestementbot.service.keyboard;

import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline.DynamicFieldValue;
import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline.InlineButtonWrappedObject;
import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline.InlineKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Map;

public interface InlineKeyboardService {

    InlineKeyboardMarkup getStaticKeyboardMarkup(String name);

    InlineKeyboardMarkup getDynamicValuesKeyboardMarkup(
            String name,
            Map<String, DynamicFieldValue> dynamicValuesMap
    );

    InlineKeyboardMarkup getDynamicFromResourceKeyboardMarkup(
            String name,
            List<? extends InlineButtonWrappedObject> objectList
    );

    InlineKeyboardMarkup getDynamicFromResourceKeyboardMarkup(
            String name,
            String baseCallback,
            List<? extends InlineButtonWrappedObject> objectList
    );

    InlineKeyboard getDynamicInlineKeyboard(String name);
}
