package com.cxf.domain.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pe_role")
@Getter
@Setter
public class Role implements Serializable {
    @Id
    private String id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 说明
     */
    private String description;
    /**
     * 组织id
     */
    private String communityId;

    /**
     * mappedBy:指向维护关系的一端
     */
    @JsonIgnore
    @ManyToMany(mappedBy="roles")
    private Set<User> users = new HashSet<User>(0);//角色与用户   多对多


    @JsonIgnore
    @ManyToMany
    @JoinTable(name="pe_role_permission",
            joinColumns={@JoinColumn(name="role_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="permission_id",referencedColumnName="id")})
    private Set<Permission> permissions = new HashSet<Permission>(0);//角色与模块  多对多
}