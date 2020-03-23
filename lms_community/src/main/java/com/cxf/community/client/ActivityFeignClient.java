package com.cxf.community.client;

import com.cxf.domain.system.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lms-activity")
public interface ActivityFeignClient {

    @RequestMapping(value = "/activity/feign/delete", method = RequestMethod.POST)
    public void delByCommunnityId(@RequestParam(value = "communityId") String communityId);
}
