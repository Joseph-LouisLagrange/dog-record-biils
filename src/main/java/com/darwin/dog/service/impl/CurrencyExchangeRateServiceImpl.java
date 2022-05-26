package com.darwin.dog.service.impl;

import com.darwin.dog.dto.in.CurrencyExchangeRateInDTO;
import com.darwin.dog.po.Coin;
import com.darwin.dog.service.inf.CurrencyExchangeRateService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class CurrencyExchangeRateServiceImpl implements CurrencyExchangeRateService {
    @Autowired
    private RestTemplate restTemplate;

    private final String exchangeRateApi = "https://api.exchangerate-api.com/v4/latest/{1}";

    /**
     * 本地线程查询缓存
     * Map[baseCoin->Map[coin->exchangeRate]]
     */
    private final ThreadLocal<Map<String, Map<String, Number>>> exchangeRateTableCache = ThreadLocal.withInitial(ConcurrentHashMap::new);

    private static Map<String, BigDecimal> exchangeRateTable = new HashMap<>();

    static {
        exchangeRateTable.put("CNY", new BigDecimal("1"));
        exchangeRateTable.put("AED", new BigDecimal("0.549"));
        exchangeRateTable.put("AFN", new BigDecimal("13.34"));
        exchangeRateTable.put("ALL", new BigDecimal("16.78"));
        exchangeRateTable.put("AMD", new BigDecimal("67.37"));
        exchangeRateTable.put("ANG", new BigDecimal("0.268"));
        exchangeRateTable.put("AOA", new BigDecimal("63.45"));
        exchangeRateTable.put("ARS", new BigDecimal("17.86"));
        exchangeRateTable.put("AUD", new BigDecimal("0.212"));
        exchangeRateTable.put("AWG", new BigDecimal("0.268"));
        exchangeRateTable.put("AZN", new BigDecimal("0.254"));
        exchangeRateTable.put("BAM", new BigDecimal("0.274"));
        exchangeRateTable.put("BBD", new BigDecimal("0.299"));
        exchangeRateTable.put("BDT", new BigDecimal("13.07"));
        exchangeRateTable.put("BGN", new BigDecimal("0.274"));
        exchangeRateTable.put("BHD", new BigDecimal("0.0562"));
        exchangeRateTable.put("BIF", new BigDecimal("304.67"));
        exchangeRateTable.put("BMD", new BigDecimal("0.149"));
        exchangeRateTable.put("BND", new BigDecimal("0.206"));
        exchangeRateTable.put("BOB", new BigDecimal("1.03"));
        exchangeRateTable.put("BRL", new BigDecimal("0.723"));
        exchangeRateTable.put("BSD", new BigDecimal("0.149"));
        exchangeRateTable.put("BTN", new BigDecimal("11.58"));
        exchangeRateTable.put("BWP", new BigDecimal("1.81"));
        exchangeRateTable.put("BYN", new BigDecimal("0.412"));
        exchangeRateTable.put("BZD", new BigDecimal("0.299"));
        exchangeRateTable.put("CAD", new BigDecimal("0.192"));
        exchangeRateTable.put("CDF", new BigDecimal("298.33"));
        exchangeRateTable.put("CHF", new BigDecimal("0.144"));
        exchangeRateTable.put("CLP", new BigDecimal("124.61"));
        exchangeRateTable.put("COP", new BigDecimal("595.88"));
        exchangeRateTable.put("CRC", new BigDecimal("101.29"));
        exchangeRateTable.put("CUP", new BigDecimal("3.59"));
        exchangeRateTable.put("CVE", new BigDecimal("15.44"));
        exchangeRateTable.put("CZK", new BigDecimal("3.45"));
        exchangeRateTable.put("DJF", new BigDecimal("26.57"));
        exchangeRateTable.put("DKK", new BigDecimal("1.04"));
        exchangeRateTable.put("DOP", new BigDecimal("8.26"));
        exchangeRateTable.put("DZD", new BigDecimal("21.82"));
        exchangeRateTable.put("EGP", new BigDecimal("2.77"));
        exchangeRateTable.put("ERN", new BigDecimal("2.24"));
        exchangeRateTable.put("ETB", new BigDecimal("7.75"));
        exchangeRateTable.put("EUR", new BigDecimal("0.14"));
        exchangeRateTable.put("FJD", new BigDecimal("0.324"));
        exchangeRateTable.put("FKP", new BigDecimal("0.119"));
        exchangeRateTable.put("FOK", new BigDecimal("1.04"));
        exchangeRateTable.put("GBP", new BigDecimal("0.119"));
        exchangeRateTable.put("GEL", new BigDecimal("0.428"));
        exchangeRateTable.put("GGP", new BigDecimal("0.119"));
        exchangeRateTable.put("GHS", new BigDecimal("1.21"));
        exchangeRateTable.put("GIP", new BigDecimal("0.119"));
        exchangeRateTable.put("GMD", new BigDecimal("8.08"));
        exchangeRateTable.put("GNF", new BigDecimal("1319.21"));
        exchangeRateTable.put("GTQ", new BigDecimal("1.15"));
        exchangeRateTable.put("GYD", new BigDecimal("31.25"));
        exchangeRateTable.put("HKD", new BigDecimal("1.17"));
        exchangeRateTable.put("HNL", new BigDecimal("3.67"));
        exchangeRateTable.put("HRK", new BigDecimal("1.05"));
        exchangeRateTable.put("HTG", new BigDecimal("16.8"));
        exchangeRateTable.put("HUF", new BigDecimal("54.19"));
        exchangeRateTable.put("IDR", new BigDecimal("2171.1"));
        exchangeRateTable.put("ILS", new BigDecimal("0.501"));
        exchangeRateTable.put("IMP", new BigDecimal("0.119"));
        exchangeRateTable.put("INR", new BigDecimal("11.58"));
        exchangeRateTable.put("IQD", new BigDecimal("218"));
        exchangeRateTable.put("IRR", new BigDecimal("6310.51"));
        exchangeRateTable.put("ISK", new BigDecimal("19.43"));
        exchangeRateTable.put("JEP", new BigDecimal("0.119"));
        exchangeRateTable.put("JMD", new BigDecimal("23.14"));
        exchangeRateTable.put("JOD", new BigDecimal("0.106"));
        exchangeRateTable.put("JPY", new BigDecimal("19"));
        exchangeRateTable.put("KES", new BigDecimal("17.51"));
        exchangeRateTable.put("KGS", new BigDecimal("12.25"));
        exchangeRateTable.put("KHR", new BigDecimal("606.66"));
        exchangeRateTable.put("KID", new BigDecimal("0.212"));
        exchangeRateTable.put("KMF", new BigDecimal("68.87"));
        exchangeRateTable.put("KRW", new BigDecimal("189.55"));
        exchangeRateTable.put("KWD", new BigDecimal("0.0448"));
        exchangeRateTable.put("KYD", new BigDecimal("0.125"));
        exchangeRateTable.put("KZT", new BigDecimal("62.31"));
        exchangeRateTable.put("LAK", new BigDecimal("2202.06"));
        exchangeRateTable.put("LBP", new BigDecimal("225.36"));
        exchangeRateTable.put("LKR", new BigDecimal("53.38"));
        exchangeRateTable.put("LRD", new BigDecimal("22.8"));
        exchangeRateTable.put("LSL", new BigDecimal("2.35"));
        exchangeRateTable.put("LYD", new BigDecimal("0.712"));
        exchangeRateTable.put("MAD", new BigDecimal("1.42"));
        exchangeRateTable.put("MDL", new BigDecimal("2.86"));
        exchangeRateTable.put("MGA", new BigDecimal("595.12"));
        exchangeRateTable.put("MKD", new BigDecimal("8.65"));
        exchangeRateTable.put("MMK", new BigDecimal("274.86"));
        exchangeRateTable.put("MNT", new BigDecimal("468.95"));
        exchangeRateTable.put("MOP", new BigDecimal("1.21"));
        exchangeRateTable.put("MRU", new BigDecimal("5.45"));
        exchangeRateTable.put("MUR", new BigDecimal("6.4"));
        exchangeRateTable.put("MVR", new BigDecimal("2.31"));
        exchangeRateTable.put("MWK", new BigDecimal("122.26"));
        exchangeRateTable.put("MXN", new BigDecimal("2.96"));
        exchangeRateTable.put("MYR", new BigDecimal("0.657"));
        exchangeRateTable.put("MZN", new BigDecimal("9.52"));
        exchangeRateTable.put("NAD", new BigDecimal("2.35"));
        exchangeRateTable.put("NGN", new BigDecimal("62.15"));
        exchangeRateTable.put("NIO", new BigDecimal("5.35"));
        exchangeRateTable.put("NOK", new BigDecimal("1.43"));
        exchangeRateTable.put("NPR", new BigDecimal("18.52"));
        exchangeRateTable.put("NZD", new BigDecimal("0.231"));
        exchangeRateTable.put("OMR", new BigDecimal("0.0575"));
        exchangeRateTable.put("PAB", new BigDecimal("0.149"));
        exchangeRateTable.put("PEN", new BigDecimal("0.553"));
        exchangeRateTable.put("PGK", new BigDecimal("0.527"));
        exchangeRateTable.put("PHP", new BigDecimal("7.81"));
        exchangeRateTable.put("PKR", new BigDecimal("30.26"));
        exchangeRateTable.put("PLN", new BigDecimal("0.643"));
        exchangeRateTable.put("PYG", new BigDecimal("1036.75"));
        exchangeRateTable.put("QAR", new BigDecimal("0.544"));
        exchangeRateTable.put("RON", new BigDecimal("0.691"));
        exchangeRateTable.put("RSD", new BigDecimal("16.5"));
        exchangeRateTable.put("RUB", new BigDecimal("8.63"));
        exchangeRateTable.put("RWF", new BigDecimal("158.86"));
        exchangeRateTable.put("SAR", new BigDecimal("0.561"));
        exchangeRateTable.put("SBD", new BigDecimal("1.22"));
        exchangeRateTable.put("SCR", new BigDecimal("1.96"));
        exchangeRateTable.put("SDG", new BigDecimal("66.88"));
        exchangeRateTable.put("SEK", new BigDecimal("1.47"));
        exchangeRateTable.put("SGD", new BigDecimal("0.206"));
        exchangeRateTable.put("SHP", new BigDecimal("0.119"));
        exchangeRateTable.put("SLL", new BigDecimal("1935.1"));
        exchangeRateTable.put("SOS", new BigDecimal("86.41"));
        exchangeRateTable.put("SRD", new BigDecimal("3.15"));
        exchangeRateTable.put("SSP", new BigDecimal("66.32"));
        exchangeRateTable.put("STN", new BigDecimal("3.43"));
        exchangeRateTable.put("SYP", new BigDecimal("376.04"));
        exchangeRateTable.put("SZL", new BigDecimal("2.35"));
        exchangeRateTable.put("THB", new BigDecimal("5.12"));
        exchangeRateTable.put("TJS", new BigDecimal("1.87"));
        exchangeRateTable.put("TMT", new BigDecimal("0.524"));
        exchangeRateTable.put("TND", new BigDecimal("0.423"));
        exchangeRateTable.put("TOP", new BigDecimal("0.345"));
        exchangeRateTable.put("TRY", new BigDecimal("2.44"));
        exchangeRateTable.put("TTD", new BigDecimal("1.01"));
        exchangeRateTable.put("TVD", new BigDecimal("0.212"));
        exchangeRateTable.put("TWD", new BigDecimal("4.41"));
        exchangeRateTable.put("TZS", new BigDecimal("346.79"));
        exchangeRateTable.put("UAH", new BigDecimal("4.42"));
        exchangeRateTable.put("UGX", new BigDecimal("549.1"));
        exchangeRateTable.put("USD", new BigDecimal("0.149"));
        exchangeRateTable.put("UYU", new BigDecimal("6"));
        exchangeRateTable.put("UZS", new BigDecimal("1655.2"));
        exchangeRateTable.put("VES", new BigDecimal("0.745"));
        exchangeRateTable.put("VND", new BigDecimal("3475.47"));
        exchangeRateTable.put("VUV", new BigDecimal("17.28"));
        exchangeRateTable.put("WST", new BigDecimal("0.399"));
        exchangeRateTable.put("XAF", new BigDecimal("91.83"));
        exchangeRateTable.put("XCD", new BigDecimal("0.404"));
        exchangeRateTable.put("XDR", new BigDecimal("0.111"));
        exchangeRateTable.put("XOF", new BigDecimal("91.83"));
        exchangeRateTable.put("XPF", new BigDecimal("16.71"));
        exchangeRateTable.put("YER", new BigDecimal("37.45"));
        exchangeRateTable.put("ZAR", new BigDecimal("2.35"));
        exchangeRateTable.put("ZMW", new BigDecimal("2.56"));
        exchangeRateTable.put("ZWL", new BigDecimal("39.48"));
    }

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
        Map<Long, Double> ans = new ConcurrentHashMap<>();
        String baseCoinShortName = baseCoin.getShortName();
        // 触发提前加载
        exchangeCoins.forEach(Coin::toString);
        exchangeCoins
                .parallelStream()
                .forEach(coin -> ans.put(coin.getID(), exchange(baseCoinShortName, amount, coin.getShortName()).doubleValue()));
        return ans;
    }


    @Override
    public BigDecimal exchange(String sourceCoin, BigDecimal sourceCoinAmount, String targetCoin) {
        return sourceCoinAmount
                .multiply(exchangeRateTable.get(targetCoin))
                .divide(exchangeRateTable.get(sourceCoin), RoundingMode.CEILING);

//        Map<String, Number> exchangeRateTable = getExchangeRateTable(sourceCoin);
//        return BigDecimal.valueOf(exchangeRateTable.get(targetCoin).doubleValue()).multiply(sourceCoinAmount);
    }

}
