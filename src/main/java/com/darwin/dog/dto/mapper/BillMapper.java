package com.darwin.dog.dto.mapper;

import com.darwin.dog.constant.BillType;
import com.darwin.dog.dto.in.CreateBillInDTO;
import com.darwin.dog.dto.out.BillsBlockDTO;
import com.darwin.dog.po.Bill;
import com.darwin.dog.po.Coin;
import com.darwin.dog.po.Signory;
import com.darwin.dog.po.User;
import com.darwin.dog.repository.SignoryRepository;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE
        , imports = {LocalDateTime.class, BigDecimal.class})
public interface BillMapper extends BaseMapper {

    @Mapping(target = "amount", expression = "java(safeAmount(createBillInDTO.amount,createBillInDTO.type))")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "signory", expression = "java(getSignory(createBillInDTO.signoryID))")
    @Mapping(target = "ledger", expression = "java(getLedger(createBillInDTO.ledgerID))")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "dateTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "coin", expression = "java(getCoin(createBillInDTO.coinID))")
    @Mapping(target = "account", expression = "java(getAccount(createBillInDTO.accountID))")
    @Mapping(target = "ID", ignore = true)
        // @Mapping(target = "type",source = "createBillInDTO.type")
    Bill from(CreateBillInDTO createBillInDTO, User user);

    default BigDecimal safeAmount(Double amount, BillType billType) {
        if (billType == BillType.EXPENSE) {
            return BigDecimal.valueOf(-Math.abs(amount));
        } else {
            return BigDecimal.valueOf(Math.abs(amount));
        }
    }

    //    @Mapping(target = "bills",expression = "java(new java.util.ArrayList<>(bills))")
//    @Mapping(target = "total",expression = "java(total.doubleValue())")
    BillsBlockDTO toBillsBlockDTO(BigDecimal total, List<Bill> bills, LocalDateTime dateTime, Coin coin);

}
