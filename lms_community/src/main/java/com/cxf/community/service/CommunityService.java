package com.cxf.community.service;

import com.cxf.common.utils.IdWorker;
import com.cxf.common.utils.PropertyUtils;
import com.cxf.community.dao.CommunityDao;
import com.cxf.domain.community.Community;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * 组织service层
 *
 * @author cxf
 */
@Service
public class CommunityService {

    @Autowired
    private CommunityDao communityDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增加组织
     *
     * @param community
     * @return
     */
    public Community save(Community community) {
        community.setId(idWorker.nextId() + "");
        community.setCreateTime(new Date());
        community.setAuditState("0");
        community.setState(1);
        return communityDao.save(community);
    }

    /**
     * 删除组织
     *
     * @param id
     */
    public void deleteById(String id) {
        communityDao.deleteById(id);
    }

    /**
     * 更新组织
     *
     * @param community
     * @return
     */
    public Community update(Community community) {
        Community target = communityDao.findById(community.getId()).get();
        //排除为null的属性后进行复制
        BeanUtils.copyProperties(community,target, PropertyUtils.getNullPropertyNames(community));
        return communityDao.save(target);
    }

    /**
     * 查找所有组织
     *
     * @return
     */
    public List<Community> findAll() {
        return communityDao.findAll();
    }

    /**
     * 根据id查找一个组织
     *
     * @param id
     * @return
     */
    public Community findById(String id) {
        return communityDao.findById(id).get();
    }
}
