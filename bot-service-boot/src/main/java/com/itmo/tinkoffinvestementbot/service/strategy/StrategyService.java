package com.itmo.tinkoffinvestementbot.service.strategy;

import tinkoffinvestementbot.dto.bot.ResponseStrategyInfoDto;

import java.util.HashMap;
import java.util.List;

public interface StrategyService {
    List<ResponseStrategyInfoDto> getAllInfo();

    HashMap<String, String> getAdditionalParamsByStrategyId(Long strategyId);
}
