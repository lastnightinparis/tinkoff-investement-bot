package tinkoffinvestementbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class User {
    @Id
    @Column
    private Long id;
    @Column
    private Long telegramChatId;
    @Column
    private String accountId;
    @Column
    private String token;
}
