package com.darwin.dog.service.impl;

import com.darwin.dog.dto.in.CurrencyExchangeRateInDTO;
import com.darwin.dog.po.Coin;
import com.darwin.dog.service.inf.CurrencyExchangeRateService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrencyExchangeRateServiceImpl implements CurrencyExchangeRateService {
    @Autowired
    private RestTemplate restTemplate;

    private final String exchangeRateApi = "https://api.exchangerate-api.com/v4/latest/{1}";

    /**
     * 本地线程查询缓存
     * Map[baseCoin->Map[coin->exchangeRate]]
     */
    private final ThreadLocal<Map<String, Map<String, Number>>> exchangeRateTableCache = ThreadLocal.withInitial(ConcurrentHashMap::new);

    @Override
    public Map<String, Number> getExchangeRateTable(String baseCoin) {
        Map<String, Number> exchangeRateTable = exchangeRateTableCache.get().get(baseCoin);
        if (exchangeRateTable == null) {
            exchangeRateTable = (Map<String, Number>) restTemplate.getForObject(exchangeRateApi, Map.class, baseCoin).get("rates");
            exchangeRateTableCache.get().put(baseCoin, exchangeRateTable);
        }
        return exchangeRateTable;
    }

    @Override
    public Map<Long, Double> currencyExchangeRate(Coin baseCoin, BigDecimal amount,
                                                  List<Coin> exchangeCoins) {
        Map<Long,Double> ans = new HashMap<>();
        for (Coin coin : exchangeCoins) {
            ans.put(coin.getID(), exchange(baseCoin.getShortName(),amount,coin.getShortName()).doubleValue());
        }
        return ans;
    }

    @Override
    public BigDecimal exchange(String sourceCoin, BigDecimal sourceCoinAmount, String targetCoin) {
        Map<String, Number> exchangeRateTable = getExchangeRateTable(sourceCoin);
        return BigDecimal.valueOf(exchangeRateTable.get(targetCoin).doubleValue()).multiply(sourceCoinAmount);
    }

}
