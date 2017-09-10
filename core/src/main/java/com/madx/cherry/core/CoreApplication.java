package com.madx.cherry.core;

import com.google.gson.Gson;
import com.madx.cherry.core.common.EnvironmentConfig;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.shiro.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Date;


@SpringBootApplication
@EnableTransactionManagement
//@EnableEurekaClient
public class CoreApplication implements CommandLineRunner{
	
	private static Logger logger = LoggerFactory.getLogger(CoreApplication.class);

	public static void main(String[] args) {
		System.out.println("*********************************************************");
		System.out.println("core 核心启动中");
		SpringApplication app = new SpringApplication(CoreApplication.class);
		
		app.setAdditionalProfiles(EnvironmentConfig.getActiveProfile());
		
		app.run(args);
		
		//  ApplicationContext applicationContext = 
		
	}
	
	@Autowired
	private SysUserService userService;


	/**
	 * 运行一次性任务
	 */
	@Override
	public void run(String... args) throws Exception {
		// init admin
		SysUserPO userPO = new SysUserPO();
		userPO.setLoginName("madx");
		userPO.setName("马哈哈");
		userPO.setPassword("9295");
		userPO.setOpenId("oEYIP0wamw-fSnN103-JYGN5eHq8");

		userPO.setCreator(1);
		userPO.setCreationTime(new Date());
		Gson gson = new Gson();
		System.out.println(gson.toJson(userPO));
		 userPO = userService.create(userPO);
		System.out.println("init complete....");
		System.out.println(gson.toJson(userPO));
		
	}
}
