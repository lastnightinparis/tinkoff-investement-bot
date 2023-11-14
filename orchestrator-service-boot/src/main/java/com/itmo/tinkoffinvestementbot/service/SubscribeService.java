package com.itmo.tinkoffinvestementbot.service;

import java.util.UUID;

public interface SubscribeService {
    UUID subscribe(String ticker);

    void cancel(UUID taskId);
}
