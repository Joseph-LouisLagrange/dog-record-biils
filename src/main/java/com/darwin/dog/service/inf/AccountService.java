package com.darwin.dog.service.inf;

import com.darwin.dog.dto.in.AddAccountInDTO;
import com.darwin.dog.po.Account;
import com.darwin.dog.po.Coin;
import com.darwin.dog.service.inf.sys.BasicService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public interface AccountService extends BasicService {

    boolean removeCompletely(Set<Long> IDs);

    boolean recover(Long ID);

    boolean recover(Set<Long> IDs);

    long queryDeletedCount();

    boolean updateAccount(long ID,String name, String remark);

    boolean add(AddAccountInDTO addAccountInDTO);

    List<Account> getAll();

    boolean delete(Long ID);

    boolean update(Account account);

    boolean addBalance(Long ID, Coin coin, BigDecimal changeAmount);

    boolean subtractBalance(Long ID,Coin coin, BigDecimal changeAmount);

    BigDecimal sumAssets();

    BigDecimal debt();

    Account getOne(Long ID);
}
