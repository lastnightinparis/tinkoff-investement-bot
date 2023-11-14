package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class SubscribeController {
    private final SubscribeService subscribeService;

    @GetMapping("/subscribe/{ticker}")
    public UUID subscribe(@PathVariable String ticker) {
        return subscribeService.subscribe(ticker);
    }

    @PostMapping("/cancel/{taskId}")
    public void cancel(@PathVariable UUID taskId) {
        subscribeService.cancel(taskId);
    }
}
