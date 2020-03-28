package com.cxf.system.controller;

import com.cxf.common.controller.BaseController;
import com.cxf.common.entity.PageResult;
import com.cxf.common.entity.Result;
import com.cxf.common.entity.ResultCode;
import com.cxf.common.poi.ExcelImportUtil;
import com.cxf.common.utils.BeanMapUtils;
import com.cxf.common.utils.QiniuUploadUtil;
import com.cxf.domain.community.Community;
import com.cxf.domain.system.User;
import com.cxf.domain.system.response.ProfileResult;
import com.cxf.domain.system.response.UserResult;
import com.cxf.system.client.CommunityFeignClient;
import com.cxf.system.service.UserService;
import com.netflix.discovery.converters.Auto;
import net.sf.jasperreports.engine.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md2Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sys")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityFeignClient communityFeignClient;

    /**
     * 删除组织时删除所有用户
     *
     * @param communityId
     */
    @RequestMapping(value = "/user/feign/delete", method = RequestMethod.POST)
    public void delByCommunityId(@RequestParam(value = "communityId") String communityId){
        userService.deleteByCommunityId(communityId);
    }

    @RequestMapping(value = "/{id}/resetpwd", method = RequestMethod.PUT)
    public Result resetPwd(@PathVariable String id) {
        userService.resetPwd(id);
        return new Result(ResultCode.SUCCESS);
    }

    //创建新组织时添加默认组织管理员用户
    @RequestMapping(value = "/user/comAdmin", method = RequestMethod.POST)
    public User addComAdmin(@RequestParam(value = "mobile") String mobile,
                            @RequestParam(value = "communityId") String communityId,
                            @RequestParam(value = "communityName") String communityName) throws Exception {
        User user = new User();
        user.setMobile(mobile);
        user.setCommunityId(communityId);
        user.setCommunityName(communityName);
        User comUser = userService.addComAdmin(user);
        return comUser;
    }

    /**
     * 批量删除
     */
    @RequiresPermissions(value = "API-USER-DELLIST")
    @RequestMapping(value = "/user/del", method = RequestMethod.POST)
    public Result deletelist(@RequestBody List<User> idArr) throws Exception {
        userService.deletelist(idArr);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 打印员工pdf报表
     */
    @RequiresPermissions(value = "API-USER-PDF")
    @RequestMapping(value = "/user/{id}/pdf", method = RequestMethod.GET)
    public void pdf(@PathVariable String id) throws Exception {
        //1.引入jasper文件
        Resource resource = new ClassPathResource("templates/Leaf_Grey_Table_Based.jasper");
        FileInputStream is = new FileInputStream(resource.getFile());
        ServletOutputStream os = response.getOutputStream();
        //构造数据:
        //用户详情
        UserResult userResult = userService.findById(id);
        //用户头像
        String staffPhoto = new QiniuUploadUtil().getPrix() + id;
        Map param = new HashMap<>();
        param.put("staffPhoto", staffPhoto);
        Map<String, Object> userResultMap = BeanMapUtils.beanToMap(userResult);
        param.putAll(userResultMap);
        try {
            //2.创建JasperPrint，向jsaper文件中填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(is, param, new JREmptyDataSource());
            //3.将JasperPrint已pdf形式输出
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);
        } catch (JRException e) {
            e.printStackTrace();
        } finally {
            os.flush();
        }
    }

    /**
     * 上传用户头像
     *
     * @param id
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/upload/{id}", method = RequestMethod.POST)
    public Result upload(@PathVariable String id, @RequestParam(name = "file") MultipartFile file) throws Exception {
        //1.调用server保存图片
        String imgUrl = userService.uplaodImage(id, file);
        //2.返回数据
        return new Result(ResultCode.SUCCESS, imgUrl);
    }

    /**
     * 模板下载
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download() {
        File f = new File("E:\\IDEAprojects\\lms_parent\\lms_common\\src\\main\\java\\com\\cxf\\common\\file\\导入数据模板.xlsx");
        try (
                FileInputStream input = new FileInputStream(f);
                OutputStream out = response.getOutputStream();
        ) {
            //设置要下载的文件的名称
            response.setHeader("Content-disposition", "attachment;fileName=" + new String("导入数据模板.xlsx".getBytes("gb2312"), "ISO8859-1"));
            //通知客服文件的MIME类型
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            response.setHeader("Content-Length", String.valueOf(input.getChannel().size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入Excel，添加用户
     * 文件上传：springboot
     */
    @RequiresPermissions(value = "API-USER-IMPORT")
    @RequestMapping(value = "/user/import", method = RequestMethod.POST)
    public Result importUser(@RequestParam(name = "file") MultipartFile file) {
        try {
            //使用工具类进行导入
            List<User> list = new ExcelImportUtil(User.class).readExcel(file.getInputStream(), 2, 1);
            //3.批量保存用户
            userService.saveAll(list, communityId, communityName);
        } catch (Exception e) {
            return new Result(ResultCode.UPLOADFILEERROR);
        }
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 修改密码
     *
     * @param map
     * @return
     */
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
        User user = userService.fingByMobild(mobile);
        //用户是否存在
        if (user == null) {
            return new Result(ResultCode.USERNOTFIND);
        }
        //用户是否被禁用
        if (user.getEnableState() != 1) {
            return new Result(ResultCode.USERENABLESTATE);
        }
        //用户所在组织是否被禁用
        Community community = communityFeignClient.findComById(user.getCommunityId());
        if (community.getState() != 1){
            return new Result(ResultCode.COMMUNITYENABLESTATE);
        }
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
    @RequiresPermissions(value = "API-USER-ROLES")
    @RequestMapping(value = "/user/assignRoles", method = RequestMethod.PUT)
    public Result assignRoles(@RequestBody Map<String, Object> map) {
        System.out.println(map);
        String id = (String) map.get("id");
        List<String> roleidslist = (List<String>) map.get("roleIds");
        userService.assignRoles(id, roleidslist);
        return new Result(ResultCode.SUCCESS);
    }

    //保存用户
    @RequiresPermissions(value = "API-USER-ADD")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result add(@RequestBody User user) throws Exception {
        user.setCommunityId(communityId);
        user.setCommunityName(communityName);
        userService.save(user);
        return Result.SUCCESS();
    }

    //更新用户
    @RequiresPermissions(value = "API-USER-UPDATE")
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
        if (map.get("communityId") == null) {
            map.put("communityId", communityId);
        }
        Page<User> searchPage = userService.findAll(map, page, pagesize);
        PageResult<User> pr = new PageResult(searchPage.getTotalElements(), searchPage.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }
}
