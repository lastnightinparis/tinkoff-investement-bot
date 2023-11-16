package com.itmo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class StrategyApplication {
    public static void main(String[] args) {
        SpringApplication.run(StrategyApplication.class, args);
    }
}
