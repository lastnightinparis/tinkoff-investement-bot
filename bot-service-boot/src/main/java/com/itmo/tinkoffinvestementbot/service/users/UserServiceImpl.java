package com.itmo.tinkoffinvestementbot.service.users;

import com.itmo.tinkoffinvestementbot.exception.system.ResourceNotFoundException;
import com.itmo.tinkoffinvestementbot.model.domain.User;
import com.itmo.tinkoffinvestementbot.model.enums.bot.BotState;
import com.itmo.tinkoffinvestementbot.model.enums.exceptions.QueryType;
import com.itmo.tinkoffinvestementbot.model.enums.exceptions.ResourceType;
import com.itmo.tinkoffinvestementbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserByChatId(long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Override
    public Optional<User> findUserByTgUsername(String tgUsername) {
        return userRepository.findByTgUsername(tgUsername);
    }

    @Override
    public List<Long> getAdminChatIds() {
        return userRepository.findAdminChatIds();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.USER, QueryType.ID, id));
    }

    @Override
    public User getUserByChatId(Long chatId) {
        return findUserByChatId(chatId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceType.USER, QueryType.CHAT_ID, chatId));
    }

    @Override
    public User save(User user) {
        User userFromDb;
        if (user.getId() != null) {
            userFromDb = getUserByChatId(user.getChatId());
            BeanUtils.copyProperties(user, userFromDb, "id");
        } else {
            userFromDb = User.builder()
                    .chatId(user.getChatId())
                    .username(user.getUsername())
                    .tgUsername(user.getTgUsername())
                    .active(false)
                    .blocked(false)
                    .connectedInvestAccount(false)
                    .botState(BotState.FIRST_START)
                    .previousBotState(BotState.START)
                    .registeredAt(Instant.now())
                    .lastActivityAt(Instant.now())
                    .build();
        }
        return userRepository.saveAndFlush(userFromDb);
    }

    @Override
    public User setCurrentBotState(Long chatId, BotState botState) {
        User user = getUserByChatId(chatId);
        if (!botState.equals(user.getBotState())) {
            user.setPreviousBotState(user.getBotState());
            user.setBotState(botState);
            return userRepository.saveAndFlush(user);
        }
        return user;
    }

    @Override
    public boolean isNew(Long chatId) {
        boolean res = userRepository.existsByChatId(chatId);
        if (res) {
            User user = getUserByChatId(chatId);
            user.setLastActivityAt(Instant.now());
            userRepository.saveAndFlush(user);
        }
        return !res;
    }
}
