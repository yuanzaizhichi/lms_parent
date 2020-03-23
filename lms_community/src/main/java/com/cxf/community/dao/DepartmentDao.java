package com.cxf.community.dao;

import com.cxf.domain.community.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;

public interface DepartmentDao extends JpaRepository<Department, String>, JpaSpecificationExecutor<Department> {
    Department findByCodeAndCommunityId(String code, String CommunityId);

    @Transactional
    void deleteByCommunityId(String id);

}
