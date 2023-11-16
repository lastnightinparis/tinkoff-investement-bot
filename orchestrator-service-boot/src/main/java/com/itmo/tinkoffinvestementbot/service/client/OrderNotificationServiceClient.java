package com.itmo.tinkoffinvestementbot.service.client;

import com.itmo.tinkoffinvestementbot.config.RestConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderState;
import tinkoffinvestementbot.dto.bot.NotificationDto;
import tinkoffinvestementbot.model.OrderStatus;
import tinkoffinvestementbot.model.TinkoffUser;

import static com.itmo.tinkoffinvestementbot.service.order.PostOrderConverter.convertMoneyValue;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderNotificationServiceClient {

    private final RestConfig restConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public void notify(TinkoffUser user, OrderStatus orderStatus, OrderState orderState) {
        val notification = new NotificationDto(user.id(), createMessage(orderStatus, orderState));
        log.info("Отправляем сообщение \"{}\"", notification.message());

//        val resourceUrl = String.join("/", restConfig.getBotUrl(), "notify");
//        restTemplate.postForObject(resourceUrl, notification, JsonNode.class);
    }

    private String getDirection(OrderState orderState) {
        return orderState.getDirection() == OrderDirection.ORDER_DIRECTION_BUY
                ? "покупку"
                : "продажу";
    }

    private String createMessage(OrderStatus orderStatus, OrderState order) {
        val direction = getDirection(order);
        return switch (orderStatus) {
            case CREATED ->
                    String.format("Отправили брокеру поручение на %s бумаги %s.\nСумма: %s.\nКоличество лотов: %s",
                            direction,
                            order.getFigi(),
                            convertMoneyValue(order.getInitialOrderPrice()),
                            order.getLotsRequested());
            case COMPLETED -> String.format("Поручение на %s бумаги %s исполнено.\nСумма: %s.\nКоличество лотов: %s",
                    direction,
                    order.getFigi(),
                    convertMoneyValue(order.getExecutedOrderPrice()),
                    order.getLotsExecuted());
            case PARTIALLY_COMPLETED ->
                    String.format("Поручение на %s бумаги %s исполнено частично.\nСумма: %s.\nКоличество лотов: %s из %s",
                            direction,
                            order.getFigi(),
                            convertMoneyValue(order.getExecutedOrderPrice()),
                            order.getLotsExecuted(),
                            order.getLotsRequested());
            case REJECTED -> "Поручение отклонили на стороне биржи";
            default -> "Не смогли подать поручение. Свяжитесь с нашим специалистом по горячей линии 8-800-555-35-35";
        };
    }
}
