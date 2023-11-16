package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tinkoffinvestementbot.dto.bot.ValidateTokenResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/token/check")
    public ValidateTokenResponse checkToken(@RequestParam(name = "userId") Long userId, @RequestParam(name = "token") String token) {
        var checkResult = tokenService.checkToken(userId, token);
        log.info("Проверили токен {}. Результат: {}", token, checkResult);
        return new ValidateTokenResponse(checkResult);
    }

    @DeleteMapping("/token/delete")
    public void deleteToken(@RequestParam("userId") Long userId) {
        tokenService.deleteToken(userId);
    }
}
