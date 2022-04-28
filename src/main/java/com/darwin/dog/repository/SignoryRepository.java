package com.darwin.dog.repository;

import com.darwin.dog.po.Signory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignoryRepository extends JpaRepository<Signory,Long> {
}
