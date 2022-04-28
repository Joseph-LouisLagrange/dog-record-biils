package com.darwin.dog.service.inf;

import com.darwin.dog.dto.in.CurrencyExchangeRateInDTO;
import com.darwin.dog.po.Coin;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CurrencyExchangeRateService {
    /**
     * 获取汇率转换表
     * @param baseCoin 本位币
     * @return 汇率转换表 Map[coin->exchangeRate]
     */
    Map<String,Number> getExchangeRateTable(String baseCoin);

    Map<Long, Double> currencyExchangeRate(Coin baseCoin,
                                                  BigDecimal amount,
                                                  List<Coin> exchangeCoins);
    /**
     * 计算源货币可以换取多少金额的本位币
     * @param sourceCoin 源货币
     * @param sourceCoinAmount 源货币的金额
     * @param targetCoin 目标货币
     * @return 源货币换算为目标货币的金额
     */
    BigDecimal exchange(String sourceCoin,BigDecimal sourceCoinAmount,String targetCoin);
}
