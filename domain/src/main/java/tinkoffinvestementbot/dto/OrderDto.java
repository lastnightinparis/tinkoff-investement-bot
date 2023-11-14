package tinkoffinvestementbot.dto;

import tinkoffinvestementbot.model.OrderSide;

public record OrderDto(Long userId, String instrumentId, Long quantity, OrderSide side) {
}
