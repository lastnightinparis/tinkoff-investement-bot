package com.itmo.tinkoffinvestementbot.service.keyboard;

import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline.DynamicFieldValue;
import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline.InlineButtonWrappedObject;
import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline.InlineKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InlineKeyboardServiceImpl implements InlineKeyboardService {

    @Qualifier(value = "dynamicInlineKeyboard")
    private final Map<String, InlineKeyboard> dynamicInlineKeyboardMap;

    @Qualifier(value = "staticInlineKeyboardMarkupMap")
    private final Map<String, InlineKeyboardMarkup> staticInlineKeyboardMarkupMap;

    @Override
    public InlineKeyboardMarkup getStaticKeyboardMarkup(String name) {
        if (staticInlineKeyboardMarkupMap.containsKey(name)) {
            return staticInlineKeyboardMarkupMap.get(name);
        }
        throw new IllegalArgumentException("There is no static keyboard with name - " + name);
    }

    @Override
    public InlineKeyboardMarkup getDynamicValuesKeyboardMarkup(
            String name,
            Map<String, DynamicFieldValue> dynamicValuesMap
    ) {
        if (dynamicInlineKeyboardMap.containsKey(name)) {
            return dynamicInlineKeyboardMap.get(name).generateDynamicFieldsKeyboard(dynamicValuesMap);
        }
        throw new IllegalArgumentException("There is no dynamic keyboard with name - " + name);
    }

    @Override
    public InlineKeyboardMarkup getDynamicFromResourceKeyboardMarkup(
            String name,
            List<? extends InlineButtonWrappedObject> objectList
    ) {
        if (dynamicInlineKeyboardMap.containsKey(name)) {
            return dynamicInlineKeyboardMap.get(name).generateFromResourcesKeyboard(objectList);
        }
        throw new IllegalArgumentException("There is no dynamic keyboard with name - " + name);
    }

    @Override
    public InlineKeyboardMarkup getDynamicFromResourceKeyboardMarkup(
            String name, String baseCallback,
            List<? extends InlineButtonWrappedObject> objectList
    ) {
        if (dynamicInlineKeyboardMap.containsKey(name)) {
            return dynamicInlineKeyboardMap.get(name).generateFromResourcesKeyboard(
                    objectList,
                    baseCallback
            );
        }
        throw new IllegalArgumentException("There is no dynamic keyboard with name - " + name);
    }

    @Override
    public InlineKeyboard getDynamicInlineKeyboard(String name) {
        if (dynamicInlineKeyboardMap.containsKey(name)) {
            return dynamicInlineKeyboardMap.get(name);
        }
        throw new IllegalArgumentException("There is no dynamic keyboard with name - " + name);
    }
}
