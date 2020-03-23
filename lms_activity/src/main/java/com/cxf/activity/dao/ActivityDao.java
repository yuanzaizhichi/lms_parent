package com.cxf.activity.dao;

import com.cxf.domain.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ActivityDao extends JpaRepository<Activity, String>, JpaSpecificationExecutor<Activity> {
    @Modifying
    @Transactional
    @Query(value = "update Activity a set a.state =1 where a.id=?1")
    void endAct(String id);

    @Modifying
    @Transactional
    @Query(value = "update Activity a set a.score =?2 where a.id=?1")
    void rate(String id, Integer point);

    @Transactional
    void deleteActivitiesByCommunityId(String id);
}
