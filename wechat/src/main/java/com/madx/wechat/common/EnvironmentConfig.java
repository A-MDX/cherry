package com.madx.wechat.common;

/**
 * 环境剖面 激活 与 选择
 * Created by A-mdx on 2017/7/9.
 */
public class EnvironmentConfig {
    // 激活的剖面环境变量名称
    private static final String ACTIVE_PROFILE_ENV = "madx_java_spring_active";

    // 测试环境
    public static final String DEFAULT_ACTIVE = "dev";

    // 生产环境
    public static final String ACTIVE_PROFILE_PRODUCTION = "prod";

    public static String getActiveProfile(){
        String activeProfile = System.getenv(ACTIVE_PROFILE_ENV);
        if (activeProfile == null || !ACTIVE_PROFILE_PRODUCTION.equals(activeProfile.toLowerCase())){
            activeProfile = DEFAULT_ACTIVE;
        }
        System.out.println("激活的剖面是：" + activeProfile);
        return activeProfile;
    }

}
