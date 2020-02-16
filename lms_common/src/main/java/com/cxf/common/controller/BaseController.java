package com.cxf.common.controller;

import com.cxf.domain.system.response.ProfileResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String communityId;
    protected String communityName;
    protected String mobile;


    @ModelAttribute
    public void setResAndReq(HttpServletRequest req, HttpServletResponse res) {
        this.request = req;
        this.response = res;

        //获取subject
        Subject subject = SecurityUtils.getSubject();
        //获取所有安全数据集合
        PrincipalCollection previousPrincipals = subject.getPrincipals();
        if (previousPrincipals != null && !previousPrincipals.isEmpty()) {
            //获取安全数据
            ProfileResult profileResult = (ProfileResult) previousPrincipals.getPrimaryPrincipal();
            this.communityId = profileResult.getCommunityId();
            this.communityName = profileResult.getCommunity();
            this.mobile = profileResult.getMobile();
        }
    }

}
