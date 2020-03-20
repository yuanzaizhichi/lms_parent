package com.cxf.community.dao;

import com.cxf.domain.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CommunityDao extends JpaRepository<Community, String>, JpaSpecificationExecutor<Community> {
}
