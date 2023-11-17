package tinkoffinvestementbot.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import tinkoffinvestementbot.model.strategies.StrategyType;

@Entity
@Table(name = "strategies")
@Data
@Builder
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Strategy {
    @Id
    @Column
    private String id;

    @Enumerated(EnumType.STRING)
    private StrategyType type;

    @Column
    private String ticker;

    @Column
    private Double riskRating;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private TinkoffUser tinkoffUser;
}
