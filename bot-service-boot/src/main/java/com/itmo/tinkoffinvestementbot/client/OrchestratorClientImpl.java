package com.itmo.tinkoffinvestementbot.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tinkoffinvestementbot.dto.bot.ValidateTokenResponse;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrchestratorClientImpl implements OrchestratorClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ValidateTokenResponse checkToken(Long userId, String token) {
        String resourceUrl = String.join("/", "http://localhost:9998", "token", "check?userId={userId}&token={token}");

        Map<String, String> params = new HashMap<>();
        params.put("userId", userId.toString());
        params.put("token", token);

        final ValidateTokenResponse response = restTemplate.getForObject(resourceUrl, ValidateTokenResponse.class, params);

        return response;
    }

    @Override
    public void deleteToken(Long userId) {
        String resourceUrl = String.join("/", "http://localhost:9998", "token", "delete?userId={userId}");

        Map<String, Long> params = new HashMap<>();
        params.put("userId", userId);

        restTemplate.delete(resourceUrl, params);
    }
}
