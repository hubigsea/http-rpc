package com.ibigsea.rpc.proxy;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibigsea.rpc.config.ProviderConfig;
import com.ibigsea.rpc.container.HttpContainer;
import com.ibigsea.rpc.exception.RpcException;
import com.ibigsea.rpc.invoke.HttpInvoke;
import com.ibigsea.rpc.serizlize.JsonFormatter;
import com.ibigsea.rpc.serizlize.JsonParser;
import com.ibigsea.rpc.serizlize.Request;

/**
 * 服务提供者代理
 * 
 * @author bigsea
 *
 */
public class ProviderProxyFactory extends AbstractHandler {

	private Logger LOG = LoggerFactory.getLogger(ProviderProxyFactory.class);

	/**
	 * 提供服务需要注册,这里使用map类实现简单的注册 约定俗成的,暴漏服务是需要注册的
	 */
	private Map<Class, Object> providers = new ConcurrentHashMap<Class, Object>();

	/**
	 * 这里用来获取暴露的服务
	 */
	private static ProviderProxyFactory factory;

	private static HttpInvoke invoke = HttpInvoke.getInstance();

	/**
	 * 构造方法 注册服务 创建http容器,并启动
	 * 
	 * @param providers
	 * @param config
	 */
	public ProviderProxyFactory(Map<Class, Object> providers, ProviderConfig config) {
		this.providers = providers;
		HttpContainer container = new HttpContainer(this, config);
		container.start();
		factory = this;
		for (Map.Entry<Class, Object> entry : providers.entrySet()) {
			Log.info(entry.getKey().getSimpleName() + " register");
		}
	}

	/**
	 * 处理请求 服务消费者发送请求报文过来,服务提供者解析请求报文,通过反射执行方法
	 */
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
			throws IOException, ServletException {
		// 获取请求报文
		String data = request.getParameter("data");

		try {
			// 反序列化
			Request req = JsonParser.reqParse(data);
			// 获取到注册的服务,并通过反射执行方法
			Object res = req.invoke(ProviderProxyFactory.getInstance().getBeanByClass(req.getClazz()));
			// 返回结果
			invoke.response(JsonFormatter.resbFormatter(res), response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} catch (RpcException e) {
			e.printStackTrace();
		}

	}

	public Object getBeanByClass(Class clazz) throws RpcException {
		Object bean = providers.get(clazz);
		if (bean != null) {
			return bean;
		}
		throw new RpcException("service no register", new NullPointerException(), clazz);
	}

	public static ProviderProxyFactory getInstance() {
		return factory;
	}

}
