package com.darwin.dog.repository;

import com.darwin.dog.constant.BillType;
import com.darwin.dog.po.Account;
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
import java.util.Set;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> , JpaSpecificationExecutor<Bill> {

    void deleteAllByIDIsIn(Set<Long> IDs);

    @Query(value = "select count(1) from bill where user_id=:userID and delete_state != 0",nativeQuery = true)
    long countDeleted(@Param("userID") long userID);

    @Query(value = "select count(b) from Bill b where b.user.ID=:userID and b.deleteType=com.darwin.dog.constant.BillDeleteType.NO_DELETE")
    long countBills(@Param("userID") long userID);

    @Modifying
    @Query(value = "update bill set delete_state = (delete_state|:deleteType) where ledger_id=:ledgerID",nativeQuery = true)
    void deleteBillsInLedger(@Param("ledgerID") Long ledgerID, @Param("deleteType") int deleteType);

    @Modifying
    @Query(value = "update bill set delete_state = (delete_state|:deleteType) where account_id=:accountID",nativeQuery = true)
    void deleteBillsInAccount(@Param("accountID") Long accountID, @Param("deleteType") int deleteType);


    @Modifying
    @Query(value = "update bill set delete_state = (delete_state|:deleteType) where id = :billID",nativeQuery = true)
    void updateBillDeleteType(@Param("billID") Long billID, @Param("deleteType") int deleteType);

    @Query(value = "select count(l) from Bill l where l.ledger.ID=:ledgerID and l.deleteType=com.darwin.dog.constant.BillDeleteType.NO_DELETE")
    long countByLedgerID(@Param("ledgerID") Long ledgerID);

    @Query(value = "select * from bill where user_id=:userID and delete_state != 0",nativeQuery = true)
    List<Bill> findAllDeleted(@Param("userID") Long userID);

    List<Bill> findByUserAndDeleteType(User user,int deleteType);

    @Query("from Bill b where b.type=:billType and b.user=:user and b.dateTime between :start and :end and b.deleteType=com.darwin.dog.constant.BillDeleteType.NO_DELETE")
    List<Bill> findAllInDateTimeAndBillType(@Param("user") User user,@Param("start") LocalDateTime start,@Param("end") LocalDateTime end,@Param("billType") BillType billType, Sort sort);

    @Query("from Bill b where b.user=:user and b.dateTime between :start and :end and b.deleteType=com.darwin.dog.constant.BillDeleteType.NO_DELETE")
    List<Bill> findAllInDateTime(@Param("user") User user,@Param("start") LocalDateTime start,@Param("end") LocalDateTime end, Sort sort);

    @Query("from Bill b where b.ledger.ID = :ledgerID and b.deleteType=com.darwin.dog.constant.BillDeleteType.NO_DELETE")
    List<Bill> findAllByLedgerID(@Param("ledgerID") Long ledgerID);

    @Query("from Bill b where b.account=:account and b.dateTime between :start and :end and b.deleteType=com.darwin.dog.constant.BillDeleteType.NO_DELETE")
    List<Bill> findAllInDateTimeAndAccount(@Param("account")Account account, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("from Bill b where b.ledger=:ledger and b.dateTime between :start and :end and b.deleteType=com.darwin.dog.constant.BillDeleteType.NO_DELETE")
    List<Bill> findAllInDateTimeAndLedger(@Param("ledger") Ledger ledger,@Param("start") LocalDateTime start,@Param("end") LocalDateTime end, Sort sort);

    List<Bill> findBillsByUserEqualsAndRemarkIsLike(User user,String keyword);

    @Query("from Bill b where b.ledger.ID = :ledgerID and b.type=:billType and b.deleteType=com.darwin.dog.constant.BillDeleteType.NO_DELETE")
    List<Bill> findByLedgerIDAndBillType(@Param("ledgerID") Long ledgerID, @Param("billType")BillType billType);


}
