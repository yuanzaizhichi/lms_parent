package com.cxf.system.dao;

import com.cxf.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;

public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    @Transactional
    void deleteByCommunityId(String communityId);
}
