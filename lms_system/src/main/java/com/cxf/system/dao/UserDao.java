package com.cxf.system.dao;

import com.cxf.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User findByMobile(String mobile);

    @Modifying
    @Transactional
    @Query(value = "update User u set u.password =?2 where u.mobile=?1")
    void updatepwd(String mobile,String newpwd);

    @Modifying
    @Transactional
    @Query(value = "update User u set u.password ='10d1e8d8358d19bba147afae3d40b309' where u.id=?1")
    void resetPwd(String id);

    @Transactional
    void deleteByCommunityId(String id);
}
