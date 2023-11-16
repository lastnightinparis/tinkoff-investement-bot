package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tinkoffinvestementbot.dto.bot.ValidateTokenResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/token/check")
    public ValidateTokenResponse checkToken(@RequestParam(name = "userId") Long userId, @RequestParam(name = "token") String token) {

        return new ValidateTokenResponse(tokenService.checkToken(userId, token));
    }

    @DeleteMapping("/token/delete")
    public void deleteToken(@RequestParam("userId") Long userId) {
        tokenService.deleteToken(userId);
    }
}
