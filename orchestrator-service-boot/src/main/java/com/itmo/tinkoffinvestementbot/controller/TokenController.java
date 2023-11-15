package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/token")
    public boolean checkToken(@RequestParam(name = "token") String token) {
        return tokenService.checkToken(token);
    }
}
