package com.cxf.community.service;

import com.cxf.common.utils.BeanMapUtils;
import com.cxf.common.utils.IdWorker;
import com.cxf.common.utils.PropertyUtils;
import com.cxf.community.client.UserFeignClient;
import com.cxf.community.dao.CommunityDao;
import com.cxf.community.dao.CommunityTypeDao;
import com.cxf.domain.community.Community;
import com.cxf.domain.community.CommunityType;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.*;


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
    private CommunityTypeDao communityTypeDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserFeignClient userFeignClient;

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
        userFeignClient.addComAdmin(community.getManagerId(), community.getId(), community.getName());
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
        BeanUtils.copyProperties(community, target, PropertyUtils.getNullPropertyNames(community));
        return communityDao.save(target);
    }

    /**
     * 查找所有组织
     *
     * @return
     */
    public List<Map<String, Object>> findAll() {
        List<Community> lstCommunity = communityDao.findAll();
        //构建返回List
        List<Map<String, Object>> lstResult = new ArrayList<>();

        //循环并插入社团类型属性
        for (Community community : lstCommunity) {

            //copy值,转换为map
            Map<String, Object> map = BeanMapUtils.beanToMap(community);
            //查询社团类型数据
            CommunityType communityType = communityTypeDao.findById(community.getType()).get();
            //插入相应数据
            map.put("typeName", communityType.getName());
            //添加到返回对象中
            lstResult.add(map);
        }
        //返回
        return lstResult;
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
