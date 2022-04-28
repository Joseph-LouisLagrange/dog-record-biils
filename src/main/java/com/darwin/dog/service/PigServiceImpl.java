package com.darwin.dog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class PigServiceImpl implements PigService {

    @Override
    public void create() {
        log.info("create()");
    }

    @Override
    public void delete(long ID) {
        log.info("delete({})",ID);
    }

    @Override
    public void getByID(Long ID) {
        log.info("getByID({})",ID);
    }

    @Override
    public void update(long[] IDs) {
        log.info("update({})", Arrays.toString(IDs));
    }

    @Override
    public void deleteAll() {
        log.info("deleteAll()");
    }

    @Override
    public void getAll() {
        log.info("getAll()");
    }
}
