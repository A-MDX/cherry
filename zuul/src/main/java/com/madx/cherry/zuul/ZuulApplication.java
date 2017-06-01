package com.madx.cherry.zuul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ZuulApplication {
	private static Logger logger = LoggerFactory.getLogger(ZuulApplication.class);
	public static void main(String[] args) {
		logger.error("*********************************************************");
		logger.error("now begin start....");
		SpringApplication.run(ZuulApplication.class, args);
		logger.error("start over...");
		logger.error("*********************************************************");
	}
}
