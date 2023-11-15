package com.itmo.tinkoffinvestementbot.exception.system;

import com.itmo.tinkoffinvestementbot.exception.TradingBotException;
import com.itmo.tinkoffinvestementbot.model.enums.exceptions.QueryType;
import com.itmo.tinkoffinvestementbot.model.enums.exceptions.ResourceType;

import java.util.Collection;
import java.util.List;

public class ResourceNotFoundException extends TradingBotException {

    private ResourceType type;
    private QueryType queryType;
    private Collection<String> values;
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(ResourceType type, QueryType queryType, Object value) {
        this(type, queryType, List.of(value.toString()));
    }

    public ResourceNotFoundException(ResourceType type, QueryType queryType, String value) {
        this(type, queryType, List.of(value));
    }

    public ResourceNotFoundException(ResourceType type, QueryType queryType, Collection<String> values) {
        this.type = type;
        this.queryType = queryType;
        this.values = List.copyOf(values);
    }

    @Override
    public String getMessage() {
        return String.format("Failed to get [%s] with %s [%s]", type, queryType.getQueryName(), values);
    }
}
