package com.darwin.dog.dto.mapper;

import com.darwin.dog.config.SpringStaticFactory;
import com.darwin.dog.dto.in.AddAccountInDTO;
import com.darwin.dog.dto.out.AccountStatisticsDTO;
import com.darwin.dog.po.Account;
import com.darwin.dog.po.AccountType;
import com.darwin.dog.po.User;
import com.darwin.dog.repository.AccountTypeRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Mapper(componentModel = "spring",imports = {BigDecimal.class,LocalDateTime.class,SpringStaticFactory.class,AccountTypeRepository.class, ArrayList.class})
public interface AccountMapper extends BaseMapper {

    @Mapping(target = "coin", expression = "java(getCoin(addAccountInDTO.getCoinID()))")
    @Mapping(target = "balance",expression = "java(BigDecimal.valueOf(addAccountInDTO.getBalance()))")
    @Mapping(target = "createTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "type", expression = "java(getAccountTypeRepository().getOne(addAccountInDTO.getTypeID()))")
    @Mapping(target = "ID", ignore = true)
    @Mapping(target = "bills", expression = "java(new ArrayList<>())")
    Account from(AddAccountInDTO addAccountInDTO, User user);

    @Mapping(target = "sumAssets",expression = "java(sumAssets.doubleValue())")
    @Mapping(target = "debt",expression = "java(debt.doubleValue())")
    @Mapping(target = "netAssets",expression = "java(sumAssets.add(debt).doubleValue())")
    AccountStatisticsDTO toAccountStatisticsDTO(BigDecimal sumAssets,BigDecimal debt);

    default AccountTypeRepository getAccountTypeRepository(){
       return getBean(AccountTypeRepository.class);
    }
}
