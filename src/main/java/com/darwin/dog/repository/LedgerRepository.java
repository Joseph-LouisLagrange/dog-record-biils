package com.darwin.dog.repository;

import com.darwin.dog.po.Ledger;
import com.darwin.dog.po.User;
import lombok.NonNull;
import org.hibernate.boot.model.source.spi.Sortable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger,Long> {

    @Query(value = "from Ledger l where l.user.ID =:#{#user.ID} and l.using=true and l.deleted=false")
    Optional<Ledger> findUsingLedger(@Param("user") User user);

    @Deprecated
    @Query(value = "select sum(b.amount) from Bill b where b.ledger.ID=:ledgerID and b.deleteType=com.darwin.dog.constant.BillDeleteType.NO_DELETE")
    Optional<BigDecimal> computeSurplus(@Param("ledgerID") Long ledgerID);


    @Query(value = "from Ledger l where l.deleted=false and l.user.ID = :userID")
    List<Ledger> findNotDeleted(@Param("userID") Long userID, Sort sort);

    @Modifying
    @Query(value = "update Ledger l set l.deleted = true where l.ID = :ID")
    void safeDeleteByID(@Param("ID") Long ID);

    @Modifying
    @Query(value = "update Ledger l set l.archive = :state where l.ID=:ID")
    void updateArchiveState(@Param("ID") Long ID,@Param("state") Boolean state);
}
