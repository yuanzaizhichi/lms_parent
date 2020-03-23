package com.cxf.community.service;

import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.common.utils.BeanMapUtils;
import com.cxf.common.utils.IdWorker;
import com.cxf.common.utils.PropertyUtils;
import com.cxf.community.client.ActivityFeignClient;
import com.cxf.community.client.UserFeignClient;
import com.cxf.community.dao.CommunityDao;
import com.cxf.community.dao.CommunityTypeDao;
import com.cxf.community.dao.DepartmentDao;
import com.cxf.domain.community.Community;
import com.cxf.domain.community.CommunityType;
import com.cxf.domain.system.User;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    private DepartmentDao departmentDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    /**
     * 增加组织
     *
     * @param community
     * @return
     */
    public Community save(Community community) throws Exception {
        List<Community> lstCommunity = communityDao.findByName(community.getName());
        if (lstCommunity.size() > 0) {
            throw new Exception("组织名称已存在");
        }
        community.setId(idWorker.nextId() + "");
        community.setCreateTime(new Date());
        community.setAuditState("0");
        community.setState(1);
        User user = userFeignClient.addComAdmin(community.getManagerId(), community.getId(), community.getName());
        return communityDao.save(community);
    }

    /**
     * 删除组织
     *
     * @param id
     */
    public void deleteById(String id) {
        communityDao.deleteById(id);
        //删除该组织所有活动
        activityFeignClient.delByCommunnityId(id);
        //删除该组织所有部门
        departmentDao.deleteByCommunityId(id);
        //删除该组织所有用户
        userFeignClient.delUserByCommunityId(id);
        //删除该组织所有权限角色
        userFeignClient.delRoleByCommunityId(id);
    }

    /**
     * 更新组织
     *
     * @param community
     * @return
     */
    public Community update(Community community) throws Exception {
        List<Community> lstCommunity = communityDao.findByName(community.getName());
        if (lstCommunity.size() > 1) {
            throw new Exception("组织名称已存在");
        }
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
    public Map<String, Object> findAllByPage(Map<String, Object> map, int page, int pagesize) {

        Specification<Community> specification = new Specification<Community>() {
            @Override
            public Predicate toPredicate(Root<Community> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(map.get("name"))) {
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + map.get("name") + "%"));
                }
                if (!StringUtils.isEmpty(map.get("type"))) {
                    list.add(criteriaBuilder.equal(root.get("type").as(String.class), map.get("type")));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Community> pageCommunity = communityDao.findAll(specification, PageRequest.of(page - 1, pagesize));
        //构建返回List
        Map<String, Object> mapResult = new HashMap<>();
        //数量
        mapResult.put("total", pageCommunity.getTotalElements());

        List<Map<String, Object>> lstCommunity = new ArrayList<>();
        //循环并插入社团类型属性
        for (Community community : pageCommunity.getContent()) {
            //copy值,转换为map
            Map<String, Object> map1 = BeanMapUtils.beanToMap(community);
            if (!StringUtils.isEmpty(community.getType())) {
                //查询社团类型数据
                CommunityType communityType = communityTypeDao.findById(community.getType()).get();
                //插入相应数据
                map1.put("typeName", communityType.getName());
            }
            //添加到返回对象中
            lstCommunity.add(map1);
        }
        mapResult.put("row", lstCommunity);
        //返回
        return mapResult;
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

    public List<Community> findAll() {
        return communityDao.findAll();
    }

    public Community findComById(String communityId) {
        return communityDao.findById(communityId).get();
    }
}
