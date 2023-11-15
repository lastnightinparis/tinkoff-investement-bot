package com.itmo.tinkoffinvestementbot.model.domain;

import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "tg_username")
    private String tgUsername;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "is_blocked")
    private boolean blocked;

    @Column(name = "is_connected_account")
    private boolean connectedInvestAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "bot_state")
    private BotState botState;

    @Enumerated(EnumType.STRING)
    @Column(name = "prev_bot_state")
    private BotState previousBotState;

    @Column(name = "registered_at")
    private Instant registeredAt;

    @Column(name = "last_activity_at")
    private Instant lastActivityAt;
}
