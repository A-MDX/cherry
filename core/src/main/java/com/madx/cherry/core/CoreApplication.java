package com.madx.cherry.core;

import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.common.EnvironmentConfig;
import com.madx.cherry.core.common.dao.SysUserDao;
import com.madx.cherry.core.common.entity.SysUserPO;
import com.madx.cherry.core.wechat.bean.MongoDataPO;
import com.madx.cherry.core.wechat.bean.WechatMsgPO;
import com.madx.cherry.core.wechat.dao.MongoDataDao;
import com.madx.cherry.core.wechat.dao.WechatMsgDao;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
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


	@Autowired
	private MongoClient mongoClient;
	@Autowired
	private WechatMsgDao wechatMsgDao;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private MongoDataDao mongoDataDao;


	@Value("${msg.dailyImage.default}")
	private String dailyImageUrl;

	@Value("${msg.mongo.database}")
	private String mongoDb;

	@Value("${msg.mongo.collection.daily}")
	private String collectionDaily;

	@Value("${msg.imageUrl}")
	private String imageUrlPrefix;

	/**
	 * 运行一次性任务
	 * @param args
	 * @throws Exception
	 */
//	@Override
	public void run(String... args) throws Exception {
		logger.info("------------------------------------------------------------------------------");
		logger.info("--------------------------一次性任务 ------------------------------");

		// 1.寻找存储的数据，若没有，则继续走下去
		MongoCollection<Document> collection = mongoClient.getDatabase(mongoDb).getCollection(collectionDaily);
		List<Document> list = new ArrayList<>();
		
		// 从这一天开始
		LocalDate naDay = LocalDate.parse("2017-07-07");
		// 9
		

		
		// 2.遍历用户数组
		List<SysUserPO> users = userDao.findByStatus(CommonCode.VALID_TRUE);

		// 3.查询数据列表，开始拼装

		Date date = new Date();
		users.forEach(u -> {


			for (int i = 1; i <= 7; i++) {
				LocalDate localDate = LocalDate.now();

				localDate = localDate.minusDays(i);
			
				String yesterday = localDate.format(DateTimeFormatter.ISO_DATE);


				Document query = new Document("name", yesterday).append("status", CommonCode.VALID_TRUE);

				FindIterable<Document> iterable = collection.find(query);

				iterable.into(list);
				if (list.size() > 0){
					logger.info("已经存在了需要的数据，不需要在跑数据库加数据了。");
					continue;
				}

				Date date1 = Date.from(localDate.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
				Date date2 = Date.from(localDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
				List<WechatMsgPO> msgPOs = wechatMsgDao.findByUserAndTimeBetween(u.getLoginName(), date1, date2);
				Document dailyMsg = new Document();
				dailyMsg.append("name", yesterday).append("status", CommonCode.VALID_TRUE).append("user", u.getLoginName());
				dailyMsg.append("creationTime", date);

				final StringBuilder description = new StringBuilder();
				final String[] picUrl = {dailyImageUrl};

				List<Document> data = new ArrayList<>();

				msgPOs.forEach(m -> {
					Document item = new Document();

					// 4.生成时间
					Instant instant = m.getTime().toInstant();
					LocalDateTime time = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
					String timeStr = time.format(DateTimeFormatter.ofPattern("HH:mm"));
					item.append("time", timeStr);

					// 5.生成数据,条目
					switch (m.getType()){
						case CommonCode.WECHAT_MSG_TYPE_TEXT:
							item.append("type", "text");
							// init data
							List<String> msgList = new ArrayList<>();
							String msgData = m.getData();
							msgList.addAll(Arrays.asList(msgData.split("\n")));
							item.append("message", msgList);

							// for description
							description.append(m.getData());

							break;
						case CommonCode.WECHAT_MSG_TYPE_IMAGE:
							item.append("type", "image");
							// init url
							String jsonStr = m.getData();
							if (jsonStr.contains("{")){
								BasicDBObject json = (BasicDBObject) JSON.parse(jsonStr);

								String mediaId = json.getString("mediaId");
								MongoDataPO mongoDataPO = mongoDataDao.findByDataId(mediaId);
								String path = mongoDataPO.getPath();
								int index = path.lastIndexOf("/");
								path = path.substring(index);
								String filePath = imageUrlPrefix + path+"/"+mongoDataPO.getName();

								item.append("url", filePath);

								// picUrl
								picUrl[0] = filePath;
							}else {
								item.append("url", jsonStr);

								// picUrl
								picUrl[0] = jsonStr;
							}

							break;
						default:
							return;
					}

					data.add(item);
				});
				dailyMsg.append("data", data);

				description.append("...讲真，昨天的我好像没写。");
				int length = description.length();
				String description_str = description.toString();
				if (length > 35){
					description_str = description.substring(0, 35);
					description_str += "...";
				}
				dailyMsg.append("description", description_str)
						.append("picUrl", picUrl[0]);

				// 6.保存至数据库
				collection.insertOne(dailyMsg);


			}
		});

		logger.info("-----------------------------end genDailyMessage------------------------------");
		logger.info("------------------------------------------------------------------------------");
		
	}
}
