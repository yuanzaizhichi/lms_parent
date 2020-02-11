package com.cxf.system;


import com.cxf.common.shiro.realm.LmsRealm;
import com.cxf.common.shiro.session.CustomSessionManager;
import com.cxf.system.shiro.realm.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {
    /**
     * 创建realm域
     */
    @Bean
    public LmsRealm getRealm() {
        return new UserRealm();
    }

    /**
     * 创建安全管理器
     */
    @Bean
    public SecurityManager getSecurityManager(LmsRealm lmsRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(lmsRealm);
        //将自定义的会话管理器注册到安全管理器中
        securityManager.setSessionManager(sessionManager());
        //将自定义的redis缓存管理器注册到安全管理器中
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    /**
     * 配置shiro的过滤器工厂
     * 在web程序中，shiro进行权限控制全部都是通过一组过滤器集合进行控制
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        //通用配置
        filterFactoryBean.setLoginUrl("/autherror?code=1");//跳转登陆页面
        filterFactoryBean.setUnauthorizedUrl("/autherror?code=2");//跳转未授权页面
        //设置所有的过滤器:有顺序Map（key = 拦截的路径；value = 过滤器类型）
        Map<String, String> filterMap = new LinkedHashMap<>();

        //anon -- 匿名访问（所有人可以访问）
        filterMap.put("/sys/login", "anon");
        filterMap.put("/autherror", "anon");
//        filterMap.put("/sys/faceLogin/**", "anon");

        //authc -- 认证后访问（登陆认证成功后）
        filterMap.put("/**", "authc");

        //perms -- 使用过滤器的形式配置请求路径的依赖权限(使用注解配置授权)

        filterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return filterFactoryBean;
    }

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    /**
     * 1.redis的控制器，操作redis
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        return redisManager;
    }

    /**
     * 2.sessionDao
     */
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        return sessionDAO;
    }

    /**
     * 3.会话管理器
     */
    public DefaultWebSessionManager sessionManager() {
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * 4.缓存管理器
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 配置shiro注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
