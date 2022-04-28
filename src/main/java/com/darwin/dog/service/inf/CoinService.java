package com.darwin.dog.service.inf;

import com.darwin.dog.po.Coin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CoinService {
    /**
     * 将 json 数据导入数据库
     * @param json JSON 格式的数据列表
     */
    void importJson(String json);

    List<Coin> getAll();

    Coin getOne(String shortName);

    Coin getOne(Long ID);
}
