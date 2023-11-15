package com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline;

/**
 * Класс обертка для создания кнопок Inline клавиатуры.
 */
public abstract class InlineButtonWrappedObject {

    /**
     * Метод возвращающий текст для кнопки.
     * @return текст кнопки
     */
    public abstract String getName();

    /**
     * Метод возвращающий ключ для Callback ответа.
     * @return ключ для Callback
     */
    public abstract String getKey();

}
