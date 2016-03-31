package com.unbank.common.constants;

public class ServerConfig {
	/**
	 * ES索引库地址
	 */
	private static String serverUrl;

	/**
	 * es集群名称
	 */
	private static String clusterName;

	public static String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		ServerConfig.serverUrl = serverUrl;
	}

	public static String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		ServerConfig.clusterName = clusterName;
	}

}
