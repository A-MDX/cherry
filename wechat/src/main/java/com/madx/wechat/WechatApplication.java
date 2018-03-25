package com.madx.wechat;

import com.google.gson.Gson;
import com.madx.wechat.common.EnvironmentConfig;
import com.madx.wechat.initme.dao.InitQuestionDao;
import com.madx.wechat.initme.entity.InitQuestionItemPo;
import com.madx.wechat.initme.entity.InitQuestionPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WechatApplication implements CommandLineRunner{

	public static void main(String[] args) {

		System.out.println("*********************************************************");
		System.out.println("wechat 核心启动中");
		SpringApplication app = new SpringApplication(WechatApplication.class);
		
		app.setAdditionalProfiles(EnvironmentConfig.getActiveProfile());

		app.run(args);
		
	}

	@Autowired
	private InitQuestionDao initQuestionDao;
	
	@Override
	public void run(String... args) throws Exception {
		InitQuestionPo initQuestionPo = new InitQuestionPo();
		initQuestionPo.setText("你好吗？");
		initQuestionPo.setIndex(1);

		List<InitQuestionItemPo> items = new ArrayList<>();
		InitQuestionItemPo itemPo = new InitQuestionItemPo();
		itemPo.setTag("A");
		itemPo.setText("呵呵哒");
		items.add(itemPo);
		
		itemPo = new InitQuestionItemPo();
		itemPo.setText("哎呀吗.");
		itemPo.setTag("B");
		items.add(itemPo);
		
		initQuestionPo.setOptionItems(items);

//		initQuestionPo = initQuestionDao.save(initQuestionPo);

		Gson gson = new Gson();
		System.out.println(gson.toJson(initQuestionPo));

		System.out.println("----------------------------------------------");
		
		Page<InitQuestionPo> list = initQuestionDao.findByStatus(1, PageRequest.of(0,4, Sort.by("index")));
		list.forEach(s -> System.out.println(gson.toJson(s)));
		
	}
}
