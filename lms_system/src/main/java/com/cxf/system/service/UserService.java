package com.cxf.system.service;

import com.cxf.common.utils.IdWorker;
import com.cxf.common.utils.PropertyUtils;
import com.cxf.common.utils.QiniuUploadUtil;
import com.cxf.domain.community.Department;
import com.cxf.domain.system.Role;
import com.cxf.domain.system.User;
import com.cxf.domain.system.response.UserResult;
import com.cxf.system.client.CommunityFeignClient;
import com.cxf.system.dao.RoleDao;
import com.cxf.system.dao.UserDao;
import org.apache.shiro.crypto.hash.Md2Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private CommunityFeignClient communityFeignClient;

    @Transactional(rollbackFor = {Exception.class})
    public void resetPwd(String id) {
        userDao.resetPwd(id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public User addComAdmin(User user) {
        String id = idWorker.nextId() + "";
        user.setId(id);
        //默认密码123456
        user.setCreateTime(new Date());
        user.setPassword(new Md2Hash("123456", "cxf666", 3).toString());
        //设置为组织管理员
        user.setUsername(user.getCommunityName() + "管理员");
        user.setLevel("coAdmin");
        user.setEnableState(1);//状态
        return userDao.save(user);
    }

    /**
     * 完成用户头像上传(七牛云)
     *
     * @param id
     * @param file
     * @return
     */
    public String uplaodImage(String id, MultipartFile file) throws Exception {
        //1.根据id查询用户
        Optional<User> byId = userDao.findById(id);
        User user = byId.isPresent() ? byId.get() : null;
        String imgUrl = new QiniuUploadUtil().upload(user.getId(), file.getBytes());
        //3.更新用户头像地址
        user.setStaffPhoto(imgUrl);
        userDao.save(user);
        //4.返回地址
        return imgUrl;
    }

    /**
     * 批量保存用户
     */
    @Transactional(rollbackFor = {Exception.class})
    public void saveAll(List<User> list, String communityId, String communityName) {
        for (User user : list) {
            //默认密码
            user.setPassword(new Md2Hash("123456", "cxf666", 3).toString());
            //id
            user.setId(idWorker.nextId() + "");
            //基本属性
            user.setCommunityId(communityId);
            user.setCommunityName(communityName);
            user.setCreateTime(new Date());
            user.setEnableState(1);
            user.setLevel("user");

            //填充部门的属性
            Department department = communityFeignClient.findDptByCode(user.getDepartmentId(), communityId);
            if (department != null) {
                user.setDepartmentId(department.getId());
                user.setDepartmentName(department.getName());
            }
            userDao.save(user);
        }
    }

    /**
     * 修改密码
     *
     * @param mobile
     * @param newpwd
     */
    public void updatepwd(String mobile, String newpwd) {
        userDao.updatepwd(mobile, newpwd);
    }

    /**
     * 根据mobile查询用户
     */
    public User fingByMobild(String mobile) {
        return userDao.findByMobile(mobile);
    }

    /**
     * 分配角色
     */
    @Transactional(rollbackFor = {Exception.class})
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

    @Transactional(rollbackFor = {Exception.class})
    public User save(User user) {
        String id = idWorker.nextId() + "";
        user.setId(id);
        //默认密码123456
        user.setCreateTime(new Date());
        user.setPassword(new Md2Hash("123456", "cxf666", 3).toString());
        user.setLevel("user");
        user.setEnableState(1);//状态
        return userDao.save(user);
    }

    @Transactional(rollbackFor = {Exception.class})
    public User update(User user) {
        User target = userDao.findById(user.getId()).get();
        //防止覆盖角色列表
        user.setRoles(null);
        //排除为null的属性后进行复制
        BeanUtils.copyProperties(user, target, PropertyUtils.getNullPropertyNames(user));
        return userDao.save(target);
    }

    public UserResult findById(String id) {
        Optional<User> byId = userDao.findById(id);
        User user = byId.isPresent() ? byId.get() : null;
        return new UserResult(user);
    }

    public Page findAll(Map<String, Object> map, int page, int size) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(map.get("communityId"))) {
                    list.add(criteriaBuilder.equal(root.get("communityId").as(String.class), map.get("communityId")));
                }
                if (!StringUtils.isEmpty(map.get("query")) && map.get("query") != "") {
                    list.add(criteriaBuilder.like(root.get("username").as(String.class), "%" + map.get("query") + "%"));
                }
                if (!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class), map.get("departmentId")));
                }
                if (!StringUtils.isEmpty(map.get("selectDep"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentName").as(String.class), map.get("selectDep")));
                }
                if (!StringUtils.isEmpty(map.get("hasDept"))) {
                    if ("0".equals((String) map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    } else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        Page<User> pageUser = userDao.findAll(specification, PageRequest.of(page - 1, size));
        return pageUser;
//        Page<User> pageUser = userDao.findAll(PageRequest.of(page - 1, size));
//        return pageUser;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deletelist(List<User> idArr) {
        for (User user : idArr) {
            deleteById(user.getId());
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteByCommunityId(String communityId) {
        userDao.deleteByCommunityId(communityId);
    }
}
