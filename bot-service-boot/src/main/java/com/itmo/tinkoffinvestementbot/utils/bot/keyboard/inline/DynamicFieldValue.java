package com.itmo.tinkoffinvestementbot.utils.bot.keyboard.inline;

import lombok.Data;

@Data
public class DynamicFieldValue {

    private String text;

    private String baseCallback;
    private String postfix;
    private String link;

    public DynamicFieldValue(String text, String postfix) {
        this.text = text;
        this.postfix = postfix;
    }

    public DynamicFieldValue(String text, String baseCallback, String postfix) {
        this.text = text;
        this.baseCallback = baseCallback;
        this.postfix = postfix;
    }

    public DynamicFieldValue(String link) {
        this.link = link;
    }
}
