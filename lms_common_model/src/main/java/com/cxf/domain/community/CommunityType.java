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
 * 组织类型类
 * @author cxf
 */
@Entity
@Table(name = "co_community_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityType implements Serializable {
    //ID
    @Id
    private String id;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 类型描述
     */
    private String description;
}