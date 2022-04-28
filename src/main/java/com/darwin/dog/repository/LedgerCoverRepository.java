package com.darwin.dog.repository;

import com.darwin.dog.po.LedgerCover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerCoverRepository extends JpaRepository<LedgerCover,Long> {
}
