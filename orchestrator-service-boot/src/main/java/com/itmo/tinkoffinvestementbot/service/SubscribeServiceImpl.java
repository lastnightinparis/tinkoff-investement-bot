package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.repository.StrategyRepository;
import com.itmo.tinkoffinvestementbot.repository.TinkoffUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import tinkoffinvestementbot.model.Strategy;
import tinkoffinvestementbot.model.strategies.StrategyType;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {
    private final StrategyRepository strategyRepository;
    private final TinkoffUserRepository userRepository;

    private final Map<UUID, ScheduledFuture<?>> tasks = new IdentityHashMap<>();

    @Override
    @Transactional
    public UUID subscribe(StrategyType strategyType, Long userId, String ticker, Double riskRating) {
        val uuid = UUID.randomUUID();
        strategyRepository.save(Strategy.builder()
                .id(uuid.toString())
                .type(strategyType)
                .tinkoffUser(userRepository.getReferenceById(userId))
                .ticker(ticker)
                .riskRating(riskRating)
                .build());
        return uuid;
    }

    @Override
    public void cancel(UUID taskId) {
        tasks.computeIfPresent(taskId, (k, v) -> {
            v.cancel(false);
            return null;
        });
    }
}
