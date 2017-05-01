package com.ibigsea.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.ibigsea.rpc.config.ConsumerConfig;
import com.ibigsea.rpc.invoke.HttpInvoke;
import com.ibigsea.rpc.serizlize.JsonFormatter;
import com.ibigsea.rpc.serizlize.JsonParser;

/**
 * 服务消费者代理
 * 
 * @author bigsea
 *
 */
public class ConsumerProxyFactory implements InvocationHandler {

	/**
	 * 消费者配置
	 */
	private ConsumerConfig config;

	/**
	 * 需要通过远程调用的服务
	 */
	private String clazz;

	private static HttpInvoke invoke = HttpInvoke.getInstance();

	/**
	 * 创建一个动态代理对象,创建出来的动态代理对象会执行invoke方法
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Object create() throws ClassNotFoundException {
		Class interfaceClass = Class.forName(clazz);
		return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass }, this);
	}

	/**
	 * 动态代理对象执行该方法 获取(类信息,方法,参数)通过序列化封装成请求报文,通过http请求发送报文到服务提供者
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 获取类信息
		Class interfaceClass = proxy.getClass().getInterfaces()[0];
		// 封装成请求报文
		String req = JsonFormatter.reqFormatter(interfaceClass, method.getName(), args[0]);
		// 发送请求报文
		String resb = invoke.request(req, config);
		// 解析响应报文
		return JsonParser.resbParse(resb);
	}

	public ConsumerConfig getConfig() {
		return config;
	}

	public void setConfig(ConsumerConfig config) {
		this.config = config;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

}
