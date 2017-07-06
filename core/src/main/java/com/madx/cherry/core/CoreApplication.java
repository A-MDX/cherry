package com.madx.cherry.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableEurekaClient
public class CoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(CoreApplication.class, args);
		
	}
	
	@Override
	public void run(String... args) throws Exception {

		
	}
}
