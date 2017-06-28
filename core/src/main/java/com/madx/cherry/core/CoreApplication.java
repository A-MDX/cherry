package com.madx.cherry.core;

import com.madx.cherry.core.wechat.bean.MongoDataPO;
import com.madx.cherry.core.wechat.dao.MongoDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.FileOutputStream;

@SpringBootApplication
@EnableTransactionManagement
public class CoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@Autowired
	private MongoDataDao mongoDataDao;
	
	@Override
	public void run(String... args) throws Exception {
		MongoDataPO dataPO = mongoDataDao.findOne("5953d4ba69248f47dc3d9376");
		if (dataPO.getData() == null){
			return;
		}
		FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\A-mdx\\Desktop\\data.jpg");
		fileOutputStream.write(dataPO.getData());
		fileOutputStream.flush();
		fileOutputStream.close();
		
	}
}
