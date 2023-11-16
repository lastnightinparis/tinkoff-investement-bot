package tinkoffinvestementbot.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * Выставленное торговое поручение
 */
@Entity
@Table(name = "trade_orders")
@Data
@Builder
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class TradeOrder {
    /**
     * UUID ордера
     */
    @Id
    @Column
    private String id;
    /**
     * Статус ордера
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    /**
     * User
     */
    @ManyToOne(optional = false)
    private User user;
}
