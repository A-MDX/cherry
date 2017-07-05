package com.madx.cherry.core;

import com.madx.cherry.core.wechat.bean.MongoDataPO;
import com.madx.cherry.core.wechat.dao.MongoDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.FileOutputStream;

@SpringBootApplication
@EnableTransactionManagement
//@EnableEurekaClient
public class CoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {

		
	}
}
