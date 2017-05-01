package com.ibigsea.rpc.container;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibigsea.rpc.config.ProviderConfig;

/**
 * 利用Jetty实现简单的嵌入式Httpserver
 * 
 * @author bigsea
 *
 */
public class HttpContainer {

	private Logger LOG = LoggerFactory.getLogger(HttpContainer.class);

	private AbstractHandler httpHandler;
	private ProviderConfig providerConfig;

	/**
	 * 构造方法
	 * 
	 * @param httpHandler
	 */
	public HttpContainer(AbstractHandler httpHandler) {
		this(httpHandler, new ProviderConfig(8080));
	}

	/**
	 * 构造方法
	 * 
	 * @param httpHandler
	 * @param providerConfig
	 */
	public HttpContainer(AbstractHandler httpHandler, ProviderConfig providerConfig) {
		this.httpHandler = httpHandler;
		this.providerConfig = providerConfig;
	}

	public void start() {
		// 进行服务器配置
		Server server = new Server();
		try {
			SelectChannelConnector connector = new SelectChannelConnector();
			// 设置监听端口
			connector.setPort(providerConfig.getPort());
			// 设置handler,请求过来之后通过该handler来处理请求
			server.setHandler(httpHandler);
			server.setConnectors(new Connector[] { connector });
			server.start();
			LOG.info("容器启动~");
		} catch (Exception e) {
			LOG.error("容器启动异常~", e);
		}

	}

}
