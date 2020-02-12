package com.cxf.system.shiro.realm;

import com.cxf.common.shiro.realm.LmsRealm;
import com.cxf.domain.system.Permission;
import com.cxf.domain.system.User;
import com.cxf.domain.system.response.ProfileResult;
import com.cxf.system.dao.PermissionMenuDao;
import com.cxf.system.service.PermissionService;
import com.cxf.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登陆用realm，主要用于登陆认证
 */
public class UserRealm extends LmsRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 认证方法
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.通过uptoken获取用户手机号和密码
        UsernamePasswordToken uptoken = (UsernamePasswordToken) authenticationToken;
        String mobile = uptoken.getUsername();
        String password = new String(uptoken.getPassword());
        //2.通过用户手机号查询用户
        User user = userService.fingByMobild(mobile);
        //3.判断用户是否存在，密码是否一致
        if (user != null && user.getPassword().equals(password)) {
            //4.构造安全数据返回（安全数据：用户基本数据和权限信息 -- profileResult）
            ProfileResult result = null;
            if (user.getLevel().equals("user")) {
                result = new ProfileResult(user);
            } else {
                Map map = new HashMap();
                if ("coAdmin".equals(user.getLevel())) {
                    map.put("enVisible", "1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user, list);
            }
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result, user.getPassword(), this.getName());
            return info;
        }
        //用户名密码不匹配
        return null;
    }
}
