package com.madx.cherry.core.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by a-mdx on 2017/7/31.
 */
@Configuration
public class ShiroConfiguration {
    private static Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager manager){
        logger.info("注入Shiro的Web过滤器-->shiroFilter", ShiroFilterFactoryBean.class);
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //Shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(manager);

        //要求登录时的链接(可根据项目的URL进行替换),非必须的属性,默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        //登录成功后要跳转的连接,逻辑也可以自定义，例如返回上次请求的页面
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //用户访问未对其授权的资源时,所显示的连接
        shiroFilterFactoryBean.setUnauthorizedUrl("/pages/403");

        /*定义shiro过滤链  Map结构
         * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.getContextPath()的值来的
         * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种
         * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
         */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");

        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/webui/**", "anon");
//        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(MyRealm realm
            , HashedCredentialsMatcher matcher
            , SessionManager sessionManager
            , EhCacheManager ehCacheManager){
        logger.info("注入Shiro的Web过滤器-->securityManager", ShiroFilterFactoryBean.class);
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //告诉realm,使用credentialsMatcher加密算法类来验证密文
        realm.setCredentialsMatcher(matcher);

        //启用缓存,默认false
        realm.setCachingEnabled(true);

        //  启用身份验证缓存，即缓存AuthenticationInfo信息，默认false；
        realm.setAuthenticationCachingEnabled(true);
        //  缓存AuthenticationInfo信息的缓存名称,即配置在ehcache.xml中的cache name
        realm.setAuthenticationCacheName("authenticationCache");

        //  启用授权缓存，即缓存AuthorizationInfo信息，默认false；
        realm.setAuthorizationCachingEnabled(true);
        //  缓存AuthorizationInfo信息的缓存名称；
        realm.setAuthorizationCacheName("authorizationCache");

        //注入EhCacheManager缓存管理器
        securityManager.setCacheManager(ehCacheManager);

        // 如果不注入 sessionManager，则会使用 默认的数据
        securityManager.setSessionManager(sessionManager);

        securityManager.setRealm(realm);
        return securityManager;
    }

    // sessionManager bean
    @Bean
    public SessionManager sessionManager(SessionDAO sessionDAO
            , SessionValidationScheduler sessionValidationScheduler){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 30 min session 过期时间
        sessionManager.setGlobalSessionTimeout(30*60*1000);
        // 无效 session
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);

        // 设置验证器
        sessionManager.setSessionValidationScheduler(sessionValidationScheduler);

        // session dao
        sessionManager.setSessionDAO(sessionDAO);

        return sessionManager;
    }

    // 会话验证调度器 <!-- 会话验证调度器 -->
    @Bean
    public SessionValidationScheduler sessionValidationScheduler(){
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        // 30 min
        scheduler.setInterval(1 * 60 * 1000);

        return scheduler;
    }

    /**
     * session 会话存储
     * @return
     */
    @Bean
    public SessionDAO sessionDAO(){
        // 已默认为此
        //<property name="activeSessionsCacheName" value="shiro-activeSessionCache"/>
        //<property name="sessionIdGenerator" ref="sessionIdGenerator"/>
        return new EnterpriseCacheSessionDAO();
    }



    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * 可以扩展凭证匹配器，实现 输入密码错误次数后锁定等功能，下一次
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");  // xxx
        matcher.setHashIterations(2);  // 加密 2 次
        //storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        matcher.setStoredCredentialsHexEncoded(true);
        return matcher;
    }

    @Bean
    public EhCacheManager ehCacheManager(){
        logger.info("注入Shiro的缓存管理器-->ehCacheManager", EhCacheManager.class);
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }

    /**
     * Shiro生命周期处理器
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    // 注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager manager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(manager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 添加ShiroDialect 为了在thymeleaf里使用shiro的标签的bean
     * @return
     */
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

}
