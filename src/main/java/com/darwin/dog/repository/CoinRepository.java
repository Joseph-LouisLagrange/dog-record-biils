package com.darwin.dog.repository;

import com.darwin.dog.po.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<Coin,Long> {
    Coin findCoinByShortNameEquals(String shortName);
}
