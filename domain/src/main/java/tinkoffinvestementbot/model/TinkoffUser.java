package tinkoffinvestementbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Данные о пользователе Тинькофф API
 */
@Entity
@Table(name = "tinkoff_users")
@Data
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class TinkoffUser {
    @Id
    @Column
    private Long id;
    @Column
    private String accountId;
    @Column
    private String token;
}
