package com.cxf.activity.controller;

import com.cxf.activity.service.ActivityService;
import com.cxf.common.controller.BaseController;
import com.cxf.common.entity.PageResult;
import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.domain.activity.Activity;
import com.cxf.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/activity")
public class ActivityController extends BaseController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/{id}/endact", method = RequestMethod.PUT)
    public Result endAct(@PathVariable String id) {
        activityService.endAct(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result save(@RequestBody Activity activity) {
        activity.setCommunityId(communityId);
        activityService.save(activity);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Result findByPage(int page, int pagesize, @RequestParam Map<String, Object>
            map) throws Exception {
        map.put("communityId", communityId);
        Page<User> searchPage = activityService.findAll(map, page, pagesize);
        PageResult<User> pr = new PageResult(searchPage.getTotalElements(), searchPage.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Activity activity = activityService.findById(id);
        return new Result(ResultCode.SUCCESS, activity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable String id, @RequestBody Activity activity) {
        activity.setId(id);
        activityService.update(activity);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        activityService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
