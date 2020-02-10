package com.cxf.domain.community;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 组织类
 * @author cxf
 */
@Entity
@Table(name = "co_community")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Community implements Serializable {
    //ID
    @Id
    private String id;
    /**
     * 组织名称
     */
    private String name;
    /**
     * 组织登录账号ID
     */
    private String managerId;
    /**
     * 当前版本
     */
    private String version;
    /**
     * 组织地址
     */
    private String communityAddress;
    /**
     * 联系人
     */
    private String principal;
    /**
     * 联系电话
     */
    private String communityPhone;
    /**
     * 邮箱
     */
    private String mailbox;
    /**
     * 组织规模
     */
    private String communitySize;
    /**
     * 所属类型
     */
    private String type;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 审核状态
     */
    private String auditState;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date createTime;
}