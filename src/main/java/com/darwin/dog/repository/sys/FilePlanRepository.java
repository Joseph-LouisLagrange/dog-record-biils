package com.darwin.dog.repository.sys;

import com.darwin.dog.po.sys.FilePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilePlanRepository extends JpaRepository<FilePlan,Long> {
}
