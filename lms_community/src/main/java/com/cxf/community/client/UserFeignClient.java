package com.cxf.community.client;

import com.cxf.domain.system.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lms-system")
public interface UserFeignClient {

    @RequestMapping(value = "sys/user/comAdmin", method = RequestMethod.POST)
    public User addComAdmin(@RequestParam(value = "mobile") String mobile,
                            @RequestParam(value = "communityId") String communityId,
                            @RequestParam(value = "communityName") String communityName);
}
