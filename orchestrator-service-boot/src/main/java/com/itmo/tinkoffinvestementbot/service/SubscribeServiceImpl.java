package com.itmo.tinkoffinvestementbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {
    private final TransmitterService transmitterService;
    private final Map<UUID, ScheduledFuture<?>> tasks = new IdentityHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    @Override
    public UUID subscribe(String ticker) {
        final UUID uuid = UUID.randomUUID();

        final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(
                () -> transmitterService.transmit(ticker),
                0L,
                1L,
                TimeUnit.SECONDS
        );

        tasks.put(uuid, scheduledFuture);
        return uuid;
    }

    @Override
    public void cancel(UUID taskId) {
        tasks.computeIfPresent(taskId, (k, v) -> {v.cancel(false); return null;});
    }
}
