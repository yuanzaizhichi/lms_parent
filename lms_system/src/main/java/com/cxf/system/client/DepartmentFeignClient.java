package com.cxf.system.client;

import com.cxf.domain.community.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 声明接口，通过feign调用其他微服务
 */
//声明调用的微服务名称
@FeignClient("lms-community")
public interface DepartmentFeignClient {

    /**
     * 调用微服务的接口
     */
    @RequestMapping(value="/community/department/search",method = RequestMethod.POST)
    public Department findByCode(@RequestParam(value = "code") String code, @RequestParam(value = "communityId") String communityId);
}
