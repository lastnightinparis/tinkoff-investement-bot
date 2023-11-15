package com.itmo.tinkoffinvestementbot.model.dto.trading;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TempTradingOrderDto {

    private Long strategyId;
    private String ticker;
    private String lastAdditionalParam;
    private Map<String, String> additionalParamNames;
    private Map<String, String> additionalParamValues;

    public TempTradingOrderDto(
            Long strategyId,
            Map<String, String> additionalParamNames
    ) {
        this.strategyId = strategyId;
        this.additionalParamNames = additionalParamNames;
        this.additionalParamValues = new HashMap<>();
    }

    public void addLastUsedParamValue(String value) {
        additionalParamValues.put(lastAdditionalParam, value);
    }
}
