package com.itmo.tinkoffinvestementbot.service;

public interface TokenService {
    boolean checkToken(Long userId, String token);
    void deleteToken(Long userId);
}
