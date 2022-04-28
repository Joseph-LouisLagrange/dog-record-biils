package com.darwin.dog.repository;

import com.darwin.dog.po.Account;
import com.darwin.dog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    @Query("from Account a where a.user=:user order by a.createTime desc ")
    List<Account> findMyAll(@Param("user") User user);

    @Query("select sum(a.balance) from Account a where a.user=:user and a.balance > 0")
    Optional<BigDecimal> computeSumAssets(@Param("user") User user);

    @Query("select sum(a.balance) from Account a where a.user=:user and a.balance < 0")
    Optional<BigDecimal> computeDebt(@Param("user") User user);

}
