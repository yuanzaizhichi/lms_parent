package com.cxf.community.dao;

import com.cxf.domain.community.CommunityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommunityTypeDao extends JpaRepository<CommunityType, String>, JpaSpecificationExecutor<CommunityType> {
}
