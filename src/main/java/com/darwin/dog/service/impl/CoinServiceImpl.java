package com.darwin.dog.service.impl;

import com.darwin.dog.constant.ObjectsCode;
import com.darwin.dog.exception.BaseExceptionType;
import com.darwin.dog.exception.CommonException;
import com.darwin.dog.po.Coin;
import com.darwin.dog.repository.CoinRepository;
import com.darwin.dog.service.inf.CoinService;
import com.darwin.dog.util.GlobalStaticBean;
import com.google.common.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Cacheable(cacheNames = "coin")
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Override
    public void importJson(String json) {
        List<Coin> coins = GlobalStaticBean.GSON.fromJson(json, new TypeToken<List<Coin>>() {
        }.getType());
        coinRepository.saveAll(coins);
    }

    public Coin getOne(String shortName){
        return coinRepository.findCoinByShortNameEquals(shortName);
    }

    @Override
    public Coin getOne(Long ID) {
        return coinRepository.getOne(ID);
    }

    @Override
    public List<Coin> getAll() {
        return coinRepository.findAll();
    }
}
