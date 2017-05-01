package com.ibigsea;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动类
 * @author bigsea
 *
 */
public class App {
	
	 public static void main(String[] args) throws Exception {
	     ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring-*.xml");
	     context.start();
	     CountDownLatch countDownLatch = new CountDownLatch(1);
	     countDownLatch.await();
	 }
	
}
