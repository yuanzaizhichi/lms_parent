package com.cxf.community.service;

import com.cxf.common.service.BaseService;
import com.cxf.common.utils.IdWorker;
import com.cxf.common.utils.PropertyUtils;
import com.cxf.community.dao.DepartmentDao;
import com.cxf.domain.community.Department;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DepartmentService extends BaseService<Department> {
    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private IdWorker idWorker;

    @Transactional(rollbackFor = {Exception.class})
    public Department save(Department department) {
        department.setId(idWorker.nextId() + "");
        department.setCreateTime(new Date());
        return departmentDao.save(department);
    }

    @Transactional(rollbackFor = {Exception.class})
    public Department update(Department department) {
        Department target = departmentDao.findById(department.getId()).get();
        //排除为null的属性后进行复制
        BeanUtils.copyProperties(department, target, PropertyUtils.getNullPropertyNames(department));
        return departmentDao.save(target);
    }

    public Department findById(String id) {
        return departmentDao.findById(id).get();
    }

    public List<Department> findAll(String communityId) {
        return departmentDao.findAll(getSpec(communityId));
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteById(String id) {
        departmentDao.deleteById(id);
    }

    /**
     * 根据部门编码和组织id查询部门
     */
    @Transactional(rollbackFor = {Exception.class})
    public Department findByCode(String code, String communityId) {
        return departmentDao.findByCodeAndCommunityId(code,communityId);
    }
}
