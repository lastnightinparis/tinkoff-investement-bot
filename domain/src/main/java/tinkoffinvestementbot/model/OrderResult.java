package tinkoffinvestementbot.model;

public record OrderResult(String id, OrderStatus status, Long fulfilledQuantity, Double commission, Double totalPrice) {
}
