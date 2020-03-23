package com.cxf.system.controller;

import com.cxf.common.controller.BaseController;
import com.cxf.common.entity.PageResult;
import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.domain.system.Role;
import com.cxf.domain.system.response.RoleResult;
import com.cxf.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/role/feign/delete", method = RequestMethod.POST)
    public void delRoleByCommunityId(@RequestParam(value = "communityId") String communityId){
        roleService.delRoleByCommunityId(communityId);
    }

    /**
     * 分配权限
     */
    @RequiresPermissions(value = "API-ROLE-PERM")
    @RequestMapping(value = "/role/assignPrem", method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String,Object> map) {
        String roleId = (String) map.get("id");
        List<String> permIdslist = (List<String>) map.get("permIds");
        roleService.assignPerms(roleId,permIdslist);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 保存角色信息
     */
    @RequiresPermissions(value = "API-ROLE-ADD")
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public Result save(@RequestBody Role role) {
        //默认公司编号为1,来自BaseController
        role.setCommunityId(communityId);
        roleService.save(role);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 获取所有角色信息
     */
    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    public Result findAll() {
        List<Role> roleList = roleService.findAll(communityId);
        return new Result(ResultCode.SUCCESS, roleList);
    }

    /**
     * 根据ID获取角色信息
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Role role = roleService.findById(id);
        RoleResult roleResult = new RoleResult(role);
        return new Result(ResultCode.SUCCESS, roleResult);
    }

    /**
     * 根据ID更新角色信息
     */
    @RequiresPermissions(value = "API-ROLE-UPDATE")
    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable String id, @RequestBody Role role) {
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据ID删除角色信息
     */
    @RequiresPermissions(value = "API-ROLE-DELETE")
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        try {
            roleService.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            return new Result(ResultCode.ROLEDELFAIL);
        }
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 分页查询角色
     */
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public Result findByPage(int page, int pagesize, Role role) {
        Page<Role> searchPage = roleService.findByPage(communityId, page, pagesize);
        PageResult<Role> pr = new PageResult(searchPage.getTotalElements(), searchPage.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }

}
