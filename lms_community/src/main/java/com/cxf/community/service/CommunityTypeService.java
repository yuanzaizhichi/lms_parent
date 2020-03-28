package com.cxf.community.service;

import com.cxf.common.utils.IdWorker;
import com.cxf.common.utils.PropertyUtils;
import com.cxf.community.dao.CommunityTypeDao;
import com.cxf.domain.community.CommunityType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 组织类型service层
 * @author cxf
 */
@Service
public class CommunityTypeService {

    @Autowired
    private CommunityTypeDao communityTypeDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增加
     *
     * @param communityType
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public CommunityType save(CommunityType communityType) {
        communityType.setId(idWorker.nextId() + "");
        return communityTypeDao.save(communityType);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Transactional(rollbackFor = {Exception.class})
    public void deleteById(String id) {
        communityTypeDao.deleteById(id);
    }

    /**
     * 更新
     *
     * @param communityType
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public CommunityType update(CommunityType communityType) {
        CommunityType TargetCommunityType = communityTypeDao.findById(communityType.getId()).get();
        //排除为null的属性后进行复制
        BeanUtils.copyProperties(communityType,TargetCommunityType, PropertyUtils.getNullPropertyNames(communityType));
        return communityTypeDao.save(TargetCommunityType);
    }

    /**
     * 查找所有
     *
     * @return
     */
    public List<CommunityType> findAll() {
        return communityTypeDao.findAll();
    }

    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    public CommunityType findById(String id) {
        return communityTypeDao.findById(id).get();
    }
}
