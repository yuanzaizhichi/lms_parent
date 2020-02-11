package com.cxf.common.controller;

import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @RequestMapping("/autherror")
    public Result autherror(int code) {
        return code == 1 ? new Result(ResultCode.UNAUTHENTICATED) : new Result(ResultCode.UNAUTHORISE);
    }
}
