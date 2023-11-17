package com.itmo.tinkoffinvestementbot.service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tinkoffinvestementbot.dto.bot.ResponseStrategyInfoDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StrategyServiceImpl implements StrategyService {

    private static final Map<Long, HashMap<String, String>> tempParamStorage = new HashMap<>();

    @Override
    public List<ResponseStrategyInfoDto> getAllInfo() {
        // TODO: Получение информации о стратегии и дополнительных параметров
        HashMap<String, String> additionalParams = new HashMap<>(
                Map.of("stop-loss", "Допустимый уровень просадки в %"));
        List<ResponseStrategyInfoDto> responseStrategyInfoDtos = List.of(
                new ResponseStrategyInfoDto(1L, "MAC", additionalParams),
                new ResponseStrategyInfoDto(2L, "MRP", additionalParams)
        );

        // Вынужденный костыль
        if (tempParamStorage.isEmpty()) {
            responseStrategyInfoDtos.forEach(info ->
                    tempParamStorage.put(info.strategyId(), info.additionalParams())
            );
        }

        return responseStrategyInfoDtos;
    }

    @Override
    public HashMap<String, String> getAdditionalParamsByStrategyId(Long strategyId) {
        if (!tempParamStorage.containsKey(strategyId))
            getAllInfo();
        return tempParamStorage.get(strategyId);
    }
}
