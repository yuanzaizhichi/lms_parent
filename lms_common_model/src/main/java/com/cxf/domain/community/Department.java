package com.cxf.domain.community;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (Department)实体类
 */
@Entity
@Table(name = "co_department")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable {
    //ID
    @Id
    private String id;
    /**
     * 组织ID
     */
    private String communityId;
    /**
     * 父级部门ID
     */
    private String parentId;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门编码，同级部门不可重复
     */
    private String code;
    /**
     * 部门类别
     */
    private String category;
    /**
     * 负责人ID
     */
    private String managerId;
    /**
     * 介绍
     */
    private String introduce;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 负责人名称
     */
    private String manager;
}
