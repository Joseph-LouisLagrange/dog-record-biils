package com.darwin.dog.service.inf;

import com.darwin.dog.dto.in.AddAccountInDTO;
import com.darwin.dog.po.Account;
import com.darwin.dog.service.inf.sys.BasicService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService extends BasicService {
    boolean add(AddAccountInDTO addAccountInDTO);
    List<Account> getAll();
    boolean delete(Long ID);
    boolean update();
    boolean changeBalance(Long ID,BigDecimal changeAmount);

    BigDecimal sumAssets();

    BigDecimal debt();
}
