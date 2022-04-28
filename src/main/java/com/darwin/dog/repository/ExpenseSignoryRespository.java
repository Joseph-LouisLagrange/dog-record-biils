package com.darwin.dog.repository;

import com.darwin.dog.po.ExpenseSignory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseSignoryRespository extends JpaRepository<ExpenseSignory,Long> {
}
