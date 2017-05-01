package com.ibigsea.rpc.config;

/**
 * 服务提供者配置
 * 
 * @author bigsea
 *
 */
public class ProviderConfig {

	/**
	 * 监听端口 服务提供者监听请求端口
	 */
	private int port;

	public ProviderConfig() {
	}

	public ProviderConfig(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
