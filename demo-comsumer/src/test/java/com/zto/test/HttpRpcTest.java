package com.zto.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ibigsea.comsumer.RefService;
import com.ibigsea.facade.HelloInterface;
import com.ibigsea.rpc.serizlize.JsonFormatter;

/**
 * 测试类
 * @author bigsea
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring-*.xml"})
public class HttpRpcTest
{
    private static final Logger logger = LoggerFactory.getLogger(HttpRpcTest.class);

    @Autowired
    private RefService service;

    @Test
    public void test() throws InterruptedException {
		service.sayHello("张三");
    }
}
