package com.darwin.dog.service.impl;

import com.darwin.dog.po.LedgerCover;
import com.darwin.dog.repository.LedgerCoverRepository;
import com.darwin.dog.service.inf.LedgerCoverService;
import com.darwin.dog.util.GlobalStaticBean;
import com.google.common.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LedgerCoverServiceImpl implements LedgerCoverService {
    @Autowired
    private LedgerCoverRepository ledgerCoverRepository;
    @Override
    public void importJson(String json) {
        ledgerCoverRepository.saveAll(GlobalStaticBean.GSON.fromJson(json,new TypeToken<List<LedgerCover>>(){}.getType()));
    }

    @Override
    public List<LedgerCover> getAll() {
        return ledgerCoverRepository.findAll();
    }
}
