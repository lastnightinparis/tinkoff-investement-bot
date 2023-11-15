package com.itmo.tinkoffinvestementbot.handler.keyboard;

import com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline.InlineButtonWrappedObject;

import java.util.List;

public interface InlineKeyboardHandler {

    List<? extends InlineButtonWrappedObject> getData(String callback);
}
