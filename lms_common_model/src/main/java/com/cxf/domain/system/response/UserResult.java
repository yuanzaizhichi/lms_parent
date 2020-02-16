package com.cxf.domain.system.response;

import com.cxf.domain.system.Role;
import com.cxf.domain.system.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserResult implements Serializable {

    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 启用状态 0为禁用 1为启用
     */
    private Integer enableState;
    /**
     * 创建时间
     */
    private Date createTime;

    private String communityId;

    private String communityName;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 加入时间
     */
    private Date timeOfEntry;

    /**
     * 学号
     */
    private String studentId;
    /**
     * 系别
     */
    private String system;


    private String departmentName;

    /**
     * level
     *     String
     *          lmsAdmin：lms管理员具备所有权限
     *          coAdmin：组织管理
     *          user：普通用户（需要分配角色）
     */
    private String level;

    private List<String> roleIds = new ArrayList<>();

    public UserResult(User user) {
        BeanUtils.copyProperties(user,this);
        for (Role role : user.getRoles()) {
            this.roleIds.add(role.getId());
        }
    }
}
