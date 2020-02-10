package com.cxf.community.controller;

import com.cxf.common.controller.BaseController;
import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.community.service.CommunityService;
import com.cxf.community.service.DepartmentService;
import com.cxf.domain.community.Community;
import com.cxf.domain.community.Department;
import com.cxf.domain.community.response.DeptListResult;
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

    @RequestMapping(value = "/department", method = RequestMethod.POST)
    public Result save(@RequestBody Department department) {
        //默认组织编号为篮球社团,来自BaseController
        department.setCommunityId(parseCommunityId());
        departmentService.save(department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department", method = RequestMethod.GET)
    public Result findAll() {
        //默认组织编号为篮球社团,来自BaseController
        Community community = communityService.findById(parseCommunityId());
        List<Department> departmentList = departmentService.findAll(parseCommunityId());
        DeptListResult deptListResult = new DeptListResult(community, departmentList);
        return new Result(ResultCode.SUCCESS, deptListResult);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Department byId = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS, byId);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable String id, @RequestBody Department department) {
        department.setId(id);
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
