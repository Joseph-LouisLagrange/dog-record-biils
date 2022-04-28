package com.darwin.dog.repository;

import com.darwin.dog.po.Bill;
import com.darwin.dog.po.Ledger;
import com.darwin.dog.po.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> , JpaSpecificationExecutor<Bill> {



    @Query(value = "select count(l) from Bill l where l.ledger.ID=:ledgerID and l.deleted=false")
    long countByLedgerID(@Param("ledgerID") Long ledgerID);

    @Query("from Bill b where b.user=:user and b.dateTime between :start and :end and b.deleted=false")
    List<Bill> findAllInDateTime(@Param("user") User user,@Param("start") LocalDateTime start,@Param("end") LocalDateTime end, Sort sort);

    @Query("from Bill b where b.ledger.ID = :ledgerID and b.deleted=false")
    List<Bill> findAllByLedgerID(@Param("ledgerID") Long ledgerID);

    @Query("from Bill b where b.ledger=:ledger and b.dateTime between :start and :end and b.deleted=false")
    List<Bill> findAllInDateTimeAndLedger(@Param("ledger") Ledger ledger,@Param("start") LocalDateTime start,@Param("end") LocalDateTime end, Sort sort);

    List<Bill> findBillsByUserEqualsAndRemarkIsLike(User user,String keyword);

    @Modifying
    @Query(value = "update Bill b set b.deleted=:state where b.ID=:ID")
    void updateDeleteState(Long ID,Boolean state);
}
