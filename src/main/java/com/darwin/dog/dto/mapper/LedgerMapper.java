package com.darwin.dog.dto.mapper;

import com.darwin.dog.dto.in.CreateLedgerInDTO;
import com.darwin.dog.dto.in.UpdateLedgerDTO;
import com.darwin.dog.dto.out.LedgerDetailDTO;
import com.darwin.dog.dto.out.LedgerRangeDetailDTO;
import com.darwin.dog.dto.out.ListLedgerOutDTO;
import com.darwin.dog.po.Bill;
import com.darwin.dog.po.Ledger;
import com.darwin.dog.po.User;
import com.darwin.dog.service.inf.LedgerService;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,uses = {BillMapper.class},injectionStrategy = InjectionStrategy.FIELD
        , imports = {LocalDateTime.class, BigDecimal.class})
public interface LedgerMapper extends BaseMapper {

    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "ID", ignore = true)
    @Mapping(target = "bills", ignore = true)
    @Mapping(target = "archive", constant = "false")
    @Mapping(target = "createTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "using", source = "createLedgerInDTO.using")
    @Mapping(target = "cover", expression = "java(getLedgerCover(createLedgerInDTO.getCoverID()))")
    @Mapping(target = "coin", expression = "java(getCoin(createLedgerInDTO.getCoinID()))")
    Ledger from(CreateLedgerInDTO createLedgerInDTO, User user);

    @Mapping(target = "sumSurplus", expression = "java(sumSurplus.doubleValue())")
    @Mapping(target = "ledgers", expression = "java(ledgers.stream().map(this::to).collect(java.util.stream.Collectors.toList()))")
    ListLedgerOutDTO to(BigDecimal sumSurplus, List<Ledger> ledgers);


    @Mapping(target = "ID", expression = "java(ledger.getID())")
    @Mapping(target = "coverUri", source = "cover.url")
    @Mapping(target = "surplus", expression = "java(getSurplus(ledger.getID()).doubleValue())")
    @Mapping(target = "billCount", expression = "java(getBillCount(ledger.getID()))")
    ListLedgerOutDTO.LedgerOutDTO to(Ledger ledger);

    default long getBillCount(Long ledgerID) {
        return getBean(LedgerService.class).countBills(ledgerID);
    }

    default BigDecimal getSurplus(Long ledgerID) {
        return getBean(LedgerService.class).surplus(ledgerID);
    }


    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "ID", ignore = true)
    @Mapping(target = "bills", ignore = true)
    @Mapping(target = "cover", expression = "java(getLedgerCover(updateLedgerDTO.getCoverID()))")
    @Mapping(target = "coin", expression = "java(getCoin(updateLedgerDTO.getCoinID()))")
    void updateLedgerDTOToLedger(UpdateLedgerDTO updateLedgerDTO, @MappingTarget Ledger ledger);


    @Mapping(target = "ID",expression = "java(ledger.getID())")
    @Mapping(target = "surplus", expression = "java(getSurplus(ledger.getID()).doubleValue())")
    @Mapping(target = "billCount", expression = "java(getBillCount(ledger.getID()))")
    LedgerDetailDTO ledgerToLedgerDetailDTO(Ledger ledger);

}
