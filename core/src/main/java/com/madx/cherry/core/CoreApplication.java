package com.madx.cherry.core;

import com.madx.cherry.core.wechat.bean.MongoDataPO;
import com.madx.cherry.core.wechat.dao.MongoDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@Autowired
	private MongoDataDao mongoDataDao;
	
	@Override
	public void run(String... args) throws Exception {
		MongoDataPO dataPO = new MongoDataPO();
		dataPO.setName("jjj");
		dataPO.setStatus(1);
		dataPO.setType("image");
		dataPO.setDataId("123ad");
		dataPO = mongoDataDao.save(dataPO);
		System.out.println(dataPO);
		
	}
}
