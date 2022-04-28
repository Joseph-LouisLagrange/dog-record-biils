package com.darwin.dog.dto.mapper;

import com.darwin.dog.config.SpringStaticFactory;
import com.darwin.dog.po.*;
import com.darwin.dog.repository.*;

public interface BaseMapper {
    default <T> T getBean(Class<T> cls){
        return SpringStaticFactory.getBean(cls);
    }

    default Coin getCoin(Long coinID){
        return getBean(CoinRepository.class).getOne(coinID);
    }

    default LedgerCover getLedgerCover(Long coverID){
        return getBean(LedgerCoverRepository.class).getOne(coverID);
    }

    default Signory getSignory(Long ID){return getBean(SignoryRepository.class).getOne(ID);}

    default Ledger getLedger(Long ID){
        return getBean(LedgerRepository.class).getOne(ID);
    }

    default Account getAccount(Long ID){
        if(ID == null){
            return null;
        }
        return getBean(AccountRepository.class).getOne(ID);
    }

}
