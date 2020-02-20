package com.cxf.activity.service;

import com.cxf.activity.dao.ActivityTypeDao;
import com.cxf.common.utils.IdWorker;
import com.cxf.common.utils.PropertyUtils;
import com.cxf.domain.activity.ActivityType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityTypeService {

    @Autowired
    private ActivityTypeDao activityTypeDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增加活动分类
     *
     * @param activityType
     * @return
     */
    public ActivityType save(ActivityType activityType) {
        activityType.setId(idWorker.nextId() + "");
        return activityTypeDao.save(activityType);
    }

    /**
     * 删除活动分类
     *
     * @param id
     */
    public void deleteById(String id) {
        activityTypeDao.deleteById(id);
    }

    /**
     * 更新活动分类
     *
     * @param activityType
     * @return
     */
    public ActivityType update(ActivityType activityType) {
        ActivityType target = activityTypeDao.findById(activityType.getId()).get();
        //排除为null的属性后进行复制
        BeanUtils.copyProperties(activityType, target, PropertyUtils.getNullPropertyNames(activityType));
        return activityTypeDao.save(target);
    }

    /**
     * 查找所有活动分类
     *
     * @return
     */
    public List<ActivityType> findAll() {
        return activityTypeDao.findAll();
    }

    /**
     * 根据id查找一个活动分类
     *
     * @param id
     * @return
     */
    public ActivityType findById(String id) {
        return activityTypeDao.findById(id).get();
    }
}
