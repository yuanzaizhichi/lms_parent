package com.cxf.system.service;

import com.cxf.common.utils.IdWorker;
import com.cxf.domain.system.Role;
import com.cxf.domain.system.User;
import com.cxf.system.dao.RoleDao;
import com.cxf.system.dao.UserDao;
import org.apache.shiro.crypto.hash.Md2Hash;
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
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RoleDao roleDao;

    /**
     * 根据mobile查询用户
     */
    public User fingByMobild(String mobile) {
        return userDao.findByMobile(mobile);
    }

    /**
     * 分配角色
     */
    public void assignRoles(String id, List<String> roleIds) {
        User user = userDao.findById(id).get();
        Set<Role> roleSet = new HashSet<>();
        for (String roleid : roleIds) {
            Role role = roleDao.findById(roleid).get();
            roleSet.add(role);
        }
        user.setRoles(roleSet);
        userDao.save(user);
    }

    public User save(User user) {
        String id = idWorker.nextId() + "";
        user.setId(id);
        //默认密码123456
        user.setCreateTime(new Date());
        user.setPassword(new Md2Hash("123456", user.getMobile(), 3).toString());
        user.setLevel("user");
        user.setEnableState(1);//状态
        return userDao.save(user);
    }

    public User update(User user) {
        User target = userDao.findById(user.getId()).get();
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        return userDao.save(target);
    }

    public User findById(String id) {
        Optional<User> byId = userDao.findById(id);
        return byId.isPresent() ? byId.get() : null;
    }

    public Page findAll(Map<String, Object> map, int page, int size) {
//        Specification<User> specification = new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> list = new ArrayList<>();
//                if (!StringUtils.isEmpty(map.get("companyId"))) {
//                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class), map.get("companyId")));
//                }
//                if (!StringUtils.isEmpty(map.get("departmentId"))) {
//                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class), map.get("departmentId")));
//                }
//                if (!StringUtils.isEmpty(map.get("hasDept"))) {
//                    if ("0".equals((String) map.get("hasDept"))) {
//                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
//                    } else {
//                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
//                    }
//                }
//                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
//            }
//        };
//
//        Page<User> pageUser = userDao.findAll(specification, PageRequest.of(page - 1, size));
//        return pageUser;
        Page<User> pageUser = userDao.findAll(PageRequest.of(page - 1, size));
        return pageUser;
    }

    public void deleteById(String id) {
        userDao.deleteById(id);
    }
}
