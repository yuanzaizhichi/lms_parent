package com.cxf.system.service;

import com.cxf.common.service.BaseService;
import com.cxf.common.utils.IdWorker;
import com.cxf.common.utils.PermissionConstants;
import com.cxf.domain.system.Permission;
import com.cxf.domain.system.Role;
import com.cxf.system.dao.PermissionDao;
import com.cxf.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService extends BaseService<Role> {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 分配权限
     */
    public void assignPerms(String id, List<String> permIdslist) {
        Role role = roleDao.findById(id).get();
        Set<Permission> permissionSet = new HashSet<>();
        for (String permIds : permIdslist) {
            Permission permission = permissionDao.findById(permIds).get();
            List<Permission> allByTypeAndPid = permissionDao.findAllByTypeAndPid(PermissionConstants.PY_API, permIds);
            permissionSet.addAll(allByTypeAndPid);
            permissionSet.add(permission);
        }
        role.setPermissions(permissionSet);
        roleDao.save(role);
    }

    /**
     * 添加角色
     */
    public void save(Role role) {
        //填充其他参数
        role.setId(idWorker.nextId() + "");
        roleDao.save(role);
    }

    /**
     * 更新角色
     */
    public void update(Role role) {
        Role targer = roleDao.getOne(role.getId());
        targer.setDescription(role.getDescription());
        targer.setName(role.getName());
        roleDao.save(targer);
    }

    public Role findById(String id) {
        return roleDao.findById(id).get();
    }

    public List<Role> findAll(String communityId) {
        return roleDao.findAll(getSpec(communityId));
    }

    public void deleteById(String id) throws DataIntegrityViolationException {
        roleDao.deleteById(id);
    }

    public Page<Role> findByPage(String companyId, int page, int size) {
        return roleDao.findAll(getSpec(companyId), PageRequest.of(page - 1, size));
    }

    public void delRoleByCommunityId(String communityId) {
        roleDao.deleteByCommunityId(communityId);
    }
}
