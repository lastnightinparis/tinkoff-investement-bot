package com.itmo.tinkoffinvestementbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tinkoffinvestementbot.model.Strategy;

import java.util.Set;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, String> {

    @Query(value = "select distinct s.ticker from Strategy s")
    Set<String> getTickers();

}
