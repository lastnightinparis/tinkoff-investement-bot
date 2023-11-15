package com.itmo.tinkoffinvestementbot.service.users;

import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findUserByChatId(long chatId);

    Optional<User> findUserByTgUsername(String tgUsername);

    List<Long> getAdminChatIds();

    User getUserById(Long id);

    User getUserByChatId(Long chatId);

    User save(User user);

    User setCurrentBotState(Long chatId, BotState botState);

    boolean isNew(Long chatId);
}
