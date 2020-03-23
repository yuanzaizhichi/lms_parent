package com.cxf.activity.controller;

import com.cxf.activity.service.ActivityService;
import com.cxf.common.controller.BaseController;
import com.cxf.common.entity.PageResult;
import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.domain.activity.Activity;
import com.cxf.domain.system.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/activity")
public class ActivityController extends BaseController {

    @Autowired
    private ActivityService activityService;

    @RequiresPermissions(value = "API-COM-SCORE")
    @RequestMapping(value = "/{id}/rate", method = RequestMethod.PUT)
    public Result rate(@PathVariable String id, @RequestBody Integer point) {
        activityService.rate(id,point);
        return new Result(ResultCode.SUCCESS);
    }

    @RequiresPermissions(value = "API-ACT-END")
    @RequestMapping(value = "/{id}/endact", method = RequestMethod.PUT)
    public Result endAct(@PathVariable String id) {
        activityService.endAct(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequiresPermissions(value = "API-ACT-ADD")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result save(@RequestBody Activity activity) {
        activity.setCommunityId(communityId);
        activity.setCommunityName(communityName);
        activityService.save(activity);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Result findByPage(int page, int pagesize, @RequestParam Map<String, Object>
            map) throws Exception {
        if (map.get("communityId") == null){
            map.put("communityId", communityId);
        }
        Page<User> searchPage = activityService.findAll(map, page, pagesize);
        PageResult<User> pr = new PageResult(searchPage.getTotalElements(), searchPage.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Activity activity = activityService.findById(id);
        return new Result(ResultCode.SUCCESS, activity);
    }

    @RequiresPermissions(value = "API-ACT-UPDATE")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable String id, @RequestBody Activity activity) {
        activity.setId(id);
        activityService.update(activity);
        return new Result(ResultCode.SUCCESS);
    }

    @RequiresPermissions(value = "API-ACT-DELETE")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        activityService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/feign/delete", method = RequestMethod.POST)
    public void delByCommunnityId(@RequestParam(value = "communityId") String communityId){
        activityService.delByCommunnityId(communityId);
    }
}
