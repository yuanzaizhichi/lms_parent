package com.cxf.community.controller;

import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.community.service.CommunityService;
import com.cxf.domain.community.Community;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    /**
     * 添加组织
     *
     * @param community
     * @return
     */
    @RequiresPermissions(value = "API-COM-ADD")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result save(@RequestBody Community community) {
        if (community != null) {
            communityService.save(community);
        }
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除组织
     *
     * @param id
     * @return
     */
    @RequiresPermissions(value = "API-COM-DELETE")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        if (!StringUtils.isEmpty(id)) {
            communityService.deleteById(id);
        }
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id更新组织
     *
     * @param id
     * @param community
     * @return
     */
    @RequiresPermissions(value = "API-COM-UPDATE")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable String id, @RequestBody Community community) {
        if (!StringUtils.isEmpty(id) && community != null) {
            community.setId(id);
            communityService.update(community);
        }
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id查找组织
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Community community = null;
        if (!StringUtils.isEmpty(id)) {
            community = communityService.findById(id);
        }
        return new Result(ResultCode.SUCCESS, community);
    }

    /**
     * 查询组织列表
     *
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Result findAll() {
        List<Map<String,Object>> lstResylt = communityService.findAll();
        return new Result(ResultCode.SUCCESS, lstResylt);
    }
}
