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
import java.util.Set;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    void deleteAllByIDIsIn(Set<Long> IDs);

    @Query("from Account a where a.user=:user and a.deleted=false order by a.createTime desc ")
    List<Account> findMyAll(@Param("user") User user);

    @Query("select count(a) from Account a where a.user=:user and a.deleted=true")
    long countDeleted(@Param("user") User user);

    List<Account> findAccountsByUserAndBalanceGreaterThanAndAndDeleted(User user,BigDecimal balance,boolean deleted);

    List<Account> findAccountsByUserAndBalanceLessThanAndDeleted(User user,BigDecimal balance,boolean deleted);

    @Modifying
    @Query("update Account a set a.deleted=true where a.ID=:ID")
    void safeDelete(@Param("ID") long ID);

}
