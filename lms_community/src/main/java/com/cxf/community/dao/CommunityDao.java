package com.cxf.community.dao;

import com.cxf.domain.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommunityDao extends JpaRepository<Community, String>, JpaSpecificationExecutor<Community> {
}
