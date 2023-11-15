package com.itmo.tinkoffinvestementbot.model.enums.exceptions;

import lombok.Getter;

@Getter
public enum QueryType {
    ID("ids"),
    CHAT_ID("chat_ids"),
    ;

    private final String queryName;
    QueryType(String queryName) {
        this.queryName = queryName;
    }
}
