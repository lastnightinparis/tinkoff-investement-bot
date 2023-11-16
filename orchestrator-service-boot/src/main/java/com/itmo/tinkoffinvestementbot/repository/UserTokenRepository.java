package com.itmo.tinkoffinvestementbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tinkoffinvestementbot.model.UserToken;
@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
}
