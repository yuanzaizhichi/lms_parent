package com.cxf.activity.service;

import com.cxf.activity.dao.ActivityDao;
import com.cxf.common.utils.IdWorker;
import com.cxf.common.utils.PropertyUtils;
import com.cxf.domain.activity.Activity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 结束活动
     * @param id
     */
    public void endAct(String id){
        activityDao.endAct(id);
    }

    /**
     * 增加活动
     *
     * @param activity
     * @return
     */
    public Activity save(Activity activity) {
        activity.setId(idWorker.nextId() + "");
        activity.setApplyTime(new Date());
        activity.setState(0);
        activity.setScore(null);
        return activityDao.save(activity);
    }

    /**
     * 删除活动
     *
     * @param id
     */
    public void deleteById(String id) {
        activityDao.deleteById(id);
    }

    /**
     * 更新活动
     *
     * @param activity
     * @return
     */
    public Activity update(Activity activity) {
        Activity target = activityDao.findById(activity.getId()).get();
        //排除为null的属性后进行复制
        BeanUtils.copyProperties(activity, target, PropertyUtils.getNullPropertyNames(activity));
        return activityDao.save(target);
    }

    /**
     * 查找所有活动
     *
     * @return
     */
    public Page findAll(Map<String, Object> map, int page, int size) {
        Specification<Activity> specification = new Specification<Activity>() {
            @Override
            public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(map.get("communityId"))) {
                    list.add(criteriaBuilder.equal(root.get("communityId").as(String.class), map.get("communityId")));
                }
                if (!StringUtils.isEmpty(map.get("query")) && map.get("query") != "") {
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + map.get("query") + "%"));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<Activity> pageActivity = activityDao.findAll(specification, PageRequest.of(page - 1, size));
        return pageActivity;
    }

    /**
     * 根据id查找一个活动
     *
     * @param id
     * @return
     */
    public Activity findById(String id) {
        return activityDao.findById(id).get();
    }
}
