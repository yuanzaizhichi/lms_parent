package com.cxf.domain.system.response;

import com.cxf.domain.system.Permission;
import com.cxf.domain.system.Role;
import com.cxf.domain.system.User;
import lombok.Getter;
import lombok.Setter;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.*;

@Setter
@Getter
public class ProfileResult implements Serializable, AuthCachePrincipal {
    private String mobile;
    private String username;
    private String community;
    private String communityId;
    private Map<String, Object> roles = new HashMap<>();

    public ProfileResult(User user, List<Permission> list) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.community = user.getCommunityName();
        this.communityId = user.getCommunityId();

        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Permission perm : list) {
            String code = perm.getCode();
            if (perm.getType() == 1) {
                menus.add(code);
            } else if (perm.getType() == 2) {
                points.add(code);
            } else {
                apis.add(code);
            }
        }
        this.roles.put("menus", menus);
        this.roles.put("points", points);
        this.roles.put("apis", apis);
    }

    public ProfileResult(User user) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.community = user.getCommunityName();
        this.communityId = user.getCommunityId();

        Set<Role> roles = user.getRoles();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Role role : roles) {
            Set<Permission> perms = role.getPermissions();
            for (Permission perm : perms) {
                String code = perm.getCode();
                if (perm.getType() == 1) {
                    menus.add(code);
                } else if (perm.getType() == 2) {
                    points.add(code);
                } else {
                    apis.add(code);
                }
            }
        }

        this.roles.put("menus", menus);
        this.roles.put("points", points);
        this.roles.put("apis", apis);
    }

    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
