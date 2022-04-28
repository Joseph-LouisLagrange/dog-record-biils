package com.darwin.dog.service.inf;

import com.darwin.dog.po.LedgerCover;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LedgerCoverService {
    void importJson(String json);

    List<LedgerCover> getAll();
}
