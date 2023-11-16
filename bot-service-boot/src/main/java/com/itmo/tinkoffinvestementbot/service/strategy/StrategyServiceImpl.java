package com.itmo.tinkoffinvestementbot.service.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tinkoffinvestementbot.dto.bot.ResponseStrategyInfoDto;
import tinkoffinvestementbot.dto.bot.ResponseStrategyWikiDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StrategyServiceImpl implements StrategyService {

    private static Map<Long, HashMap<String, String>> tempParamStorage = new HashMap<>();
    private static List<ResponseStrategyWikiDto> tempStrategiesWiki = new ArrayList<>();

    @Override
    public List<ResponseStrategyInfoDto> getAllInfo() {
        // TODO: Получение информации о стратегии и дополнительных параметров
        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put("currency", "Валюта");
        additionalParams.put("stop-loss", "Стоп-лосс");
        List<ResponseStrategyInfoDto> responseStrategyInfoDtos = List.of(
                new ResponseStrategyInfoDto(1L, "Супер стратегия 1", additionalParams),
                new ResponseStrategyInfoDto(2L, "Супер стратегия 2", additionalParams),
                new ResponseStrategyInfoDto(3L, "Супер стратегия 3", additionalParams)
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

    @Override
    public List<ResponseStrategyWikiDto> getAllWiki() {
        // TODO: Получение описания стратегий
        List<ResponseStrategyWikiDto> wikisResponse = new ArrayList<>();
        wikisResponse.add(new ResponseStrategyWikiDto(1L, "Супер стратегия 1", "Some Description 1\nSome Info"));
        wikisResponse.add(new ResponseStrategyWikiDto(2L, "Супер стратегия 2", "Some Description 2\nSome Info"));
        wikisResponse.add(new ResponseStrategyWikiDto(3L, "Супер стратегия 3", "Some Description 3\nSome Info"));

        if (tempStrategiesWiki.isEmpty()) {
            tempStrategiesWiki = wikisResponse;
        }
        return wikisResponse;
    }

    @Override
    public ResponseStrategyWikiDto getWiki(Long strategyId) {
        Optional<ResponseStrategyWikiDto> wikiDto = tempStrategiesWiki.stream()
                .filter(wiki -> wiki.strategyId().equals(strategyId))
                .findFirst();

        if (wikiDto.isEmpty()) {
            getAllWiki();
            return getWiki(strategyId);
        }
        return wikiDto.get();
    }
}
