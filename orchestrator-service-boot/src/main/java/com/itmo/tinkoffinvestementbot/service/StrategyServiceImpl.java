package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.service.client.StrategyServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tinkoffinvestementbot.dto.stratagies.RequestStrategyDto;
import tinkoffinvestementbot.dto.stratagies.ResponseStrategyDto;
import tinkoffinvestementbot.dto.stratagies.RunStrategyRequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StrategyServiceImpl implements StrategyService {
    private final StrategyServiceClient strategyServiceClient;

    @Override
    public ResponseStrategyDto getInfo(RequestStrategyDto requestStrategyDto) {
        return strategyServiceClient.getInfo(requestStrategyDto);
    }

    @Override
    public void run(RunStrategyRequestDto runStrategyRequestDto) {
        strategyServiceClient.run(runStrategyRequestDto);
    }

    @Override
    public List<ResponseStrategyDto> list() {
        return strategyServiceClient.list();
    }
}
