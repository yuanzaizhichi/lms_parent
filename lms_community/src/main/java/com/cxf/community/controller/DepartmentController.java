package com.cxf.community.controller;

import com.cxf.common.controller.BaseController;
import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.community.service.CommunityService;
import com.cxf.community.service.DepartmentService;
import com.cxf.domain.community.Community;
import com.cxf.domain.community.Department;
import com.cxf.domain.community.response.DeptListResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/community")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CommunityService communityService;

    @RequiresPermissions(value = "API-DEP-ADD")
    @RequestMapping(value = "/department", method = RequestMethod.POST)
    public Result save(@RequestBody Department department) {
        //默认组织编号为篮球社团,来自BaseController
        department.setCommunityId(communityId);
        departmentService.save(department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department", method = RequestMethod.GET)
    public Result findAll() {
        Community community = communityService.findById(communityId);
        List<Department> departmentList = departmentService.findAll(communityId);
        DeptListResult deptListResult = new DeptListResult(community, departmentList);
        return new Result(ResultCode.SUCCESS, deptListResult);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Department byId = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS, byId);
    }

    @RequiresPermissions(value = "API-DEP-UPDATE")
    @RequestMapping(value = "/department/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable String id, @RequestBody Department department) {
        department.setId(id);
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequiresPermissions(value = "API-DEP-DELETE")
    @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value="/department/search",method = RequestMethod.POST)
    public Department findByCode(@RequestParam(value="code") String code,@RequestParam(value="communityId") String communityId) {
        Department dept = departmentService.findByCode(code,communityId);
        return dept;
    }
}
