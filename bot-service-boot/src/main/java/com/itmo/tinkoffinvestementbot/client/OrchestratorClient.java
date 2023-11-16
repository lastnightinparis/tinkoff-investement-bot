package com.itmo.tinkoffinvestementbot.client;

import tinkoffinvestementbot.dto.bot.ValidateTokenResponse;

public interface OrchestratorClient {
    ValidateTokenResponse checkToken(Long userId, String token);
    void deleteToken(Long userId);
}
