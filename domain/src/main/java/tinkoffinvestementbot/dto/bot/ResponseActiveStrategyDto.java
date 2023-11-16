package tinkoffinvestementbot.dto.bot;

import lombok.Data;

import java.util.UUID;

@Data
public class ResponseActiveStrategyDto {

    UUID orderId;
    String ticker;
    /**
     * 0 - В ожидании, 1- Куплено, 2 - Продано
     */
    Integer status;
}
