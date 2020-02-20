package com.cxf.activity.dao;

import com.cxf.domain.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ActivityDao extends JpaRepository<Activity, String>, JpaSpecificationExecutor<Activity> {
}
