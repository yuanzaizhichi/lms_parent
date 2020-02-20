package com.cxf.activity.dao;

import com.cxf.domain.activity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityTypeDao extends JpaRepository<ActivityType, String> {
}
