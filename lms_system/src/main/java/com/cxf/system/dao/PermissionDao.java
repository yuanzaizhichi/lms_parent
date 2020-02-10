package com.cxf.system.dao;


import com.cxf.domain.system.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 *  * 权限数据访问接口
 *  
 */
public interface PermissionDao extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {
    List<Permission> findAllByTypeAndPid(int type, String pid);
}