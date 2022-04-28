package com.darwin.dog.repository.sys;

import com.darwin.dog.po.sys.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

}
