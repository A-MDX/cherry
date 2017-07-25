package com.madx.cherry.core;

import com.madx.cherry.core.common.EnvironmentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableEurekaClient
public class CoreApplication {
	
	private static Logger logger = LoggerFactory.getLogger(CoreApplication.class);

	public static void main(String[] args) {
		System.out.println("*********************************************************");
		System.out.println("core 核心启动中");
		SpringApplication app = new SpringApplication(CoreApplication.class);
		
		app.setAdditionalProfiles(EnvironmentConfig.getActiveProfile());
		
		app.run(args);
		
		//  ApplicationContext applicationContext = 
		
	}


	/**
	 * 运行一次性任务
	 * @param args
	 * @throws Exception
	 * 
	 */
//	@Override
	public void run(String... args) throws Exception {

		
	}
}
