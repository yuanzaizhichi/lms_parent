package com.cxf.system.controller;

import com.cxf.common.controller.BaseController;
import com.cxf.common.entity.PageResult;
import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.domain.system.User;
import com.cxf.domain.system.response.ProfileResult;
import com.cxf.domain.system.response.UserResult;
import com.cxf.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md2Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sys")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/editpwd", method = RequestMethod.POST)
    public Result editpwd(@RequestBody Map<String, Object> map) {
        User user = userService.fingByMobild(mobile);
        String oldpwd = new Md2Hash(map.get("oldpass"), "cxf666", 3).toString();
        if (!oldpwd.equals(user.getPassword())) {
            return new Result(ResultCode.OLDPWDFAIL);
        }
        String newpwd = new Md2Hash(map.get("pass"), "cxf666", 3).toString();
        userService.updatepwd(mobile, newpwd);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 登陆成功后，获取解析token获取用户信息
     */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {
        //shiro获取用户信息流程
        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        //2.获取安全数据
        ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS, result);
    }

    /**
     * 登陆
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody Map<String, Object> loginMap) {
        String mobile = (String) loginMap.get("mobile");
        String password = (String) loginMap.get("password");
        //shiro认证授权流程
        try {
            //1.构造登陆令牌
            password = new Md2Hash(password, "cxf666", 3).toString();
            UsernamePasswordToken uptoken = new UsernamePasswordToken(mobile, password);
            //2.获取subject
            Subject subject = SecurityUtils.getSubject();
            //3.调用login方法，进入realm完成认证
            subject.login(uptoken);
            //4.获取sessionId
            String sessionId = (String) subject.getSession().getId();
            //5.构造返回
            return new Result(ResultCode.SUCCESS, sessionId);
        } catch (Exception e) {
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }
    }

    /**
     * 分配角色
     */
    @RequestMapping(value = "/user/assignRoles", method = RequestMethod.PUT)
    public Result save(@RequestBody Map<String, Object> map) {
        System.out.println(map);
        String id = (String) map.get("id");
        List<String> roleidslist = (List<String>) map.get("roleIds");
        userService.assignRoles(id, roleidslist);
        return new Result(ResultCode.SUCCESS);
    }

    //保存用户
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result add(@RequestBody User user) throws Exception {
        user.setCommunityId(communityId);
        user.setCommunityName(communityName);
        userService.save(user);
        return Result.SUCCESS();
    }

    //更新用户
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(name = "id") String id, @RequestBody User user)
            throws Exception {
        userService.update(user);
        return Result.SUCCESS();
    }

    //删除用户
    @RequiresPermissions(value = "API-USER-DELETE")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(name = "id") String id) throws Exception {
        userService.deleteById(id);
        return Result.SUCCESS();
    }

    /**
     * 根据ID查询用户
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(name = "id") String id) throws Exception {
        UserResult userResult = userService.findById(id);
        return new Result(ResultCode.SUCCESS, userResult);
    }

    /**
     * 分页查询用户
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Result findByPage(int page, int pagesize, @RequestParam Map<String, Object>
            map) throws Exception {
        map.put("communityId", communityId);
        Page<User> searchPage = userService.findAll(map, page, pagesize);
        PageResult<User> pr = new PageResult(searchPage.getTotalElements(), searchPage.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }
}
