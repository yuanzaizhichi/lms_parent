package com.cxf.community.controller;

import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.community.service.CommunityTypeService;
import com.cxf.domain.community.CommunityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityTypeController {

    @Autowired
    private CommunityTypeService communityTypeService;

    /**
     * 添加
     *
     * @param communityType
     * @return
     */
    @RequestMapping(value = "/type", method = RequestMethod.POST)
    public Result save(@RequestBody CommunityType communityType) {
        if (communityType != null) {
            communityTypeService.save(communityType);
        }
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/type/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        if (!StringUtils.isEmpty(id)) {
            communityTypeService.deleteById(id);
        }
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id更新
     *
     * @param id
     * @param communityType
     * @return
     */
    @RequestMapping(value = "/type/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable String id, @RequestBody CommunityType communityType) {
        if (!StringUtils.isEmpty(id) && communityType != null) {
            communityType.setId(id);
            communityTypeService.update(communityType);
        }
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/type/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        CommunityType communityType = null;
        if (!StringUtils.isEmpty(id)) {
            communityType = communityTypeService.findById(id);
        }
        return new Result(ResultCode.SUCCESS, communityType);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @RequestMapping(value = "/type", method = RequestMethod.GET)
    public Result findAll() {
        List<CommunityType> lstCommunityType = communityTypeService.findAll();
        return new Result(ResultCode.SUCCESS, lstCommunityType);
    }
}
