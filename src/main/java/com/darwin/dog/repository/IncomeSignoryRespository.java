package com.darwin.dog.repository;

import com.darwin.dog.po.IncomeSignory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeSignoryRespository extends JpaRepository<IncomeSignory,Long> {
}
