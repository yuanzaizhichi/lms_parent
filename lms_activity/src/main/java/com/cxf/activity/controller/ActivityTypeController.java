package com.cxf.activity.controller;

import com.cxf.activity.service.ActivityTypeService;
import com.cxf.common.controller.BaseController;
import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.domain.activity.ActivityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/activity")
public class ActivityTypeController extends BaseController {

    @Autowired
    private ActivityTypeService activityTypeService;

    @RequestMapping(value = "/type", method = RequestMethod.POST)
    public Result save(@RequestBody ActivityType activityType) {
        activityTypeService.save(activityType);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/type", method = RequestMethod.GET)
    public Result findAll() throws Exception {
        List<ActivityType> activityTypes = activityTypeService.findAll();
        return new Result(ResultCode.SUCCESS, activityTypes);
    }

    @RequestMapping(value = "/type/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        ActivityType activityType = activityTypeService.findById(id);
        return new Result(ResultCode.SUCCESS, activityType);
    }

    @RequestMapping(value = "/type/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        activityTypeService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
