package com.itmo.tinkoffinvestementbot.repository;

import com.itmo.tinkoffinvestementbot.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByChatId(long chatId);

    Optional<User> findByTgUsername(String tgUsername);

    boolean existsByChatId(long chatId);

    @Query(value = "SELECT u.chat_id FROM users u WHERE u.user_role = 'ADMIN_ROLE'", nativeQuery = true)
    List<Long> findAdminChatIds();
}
