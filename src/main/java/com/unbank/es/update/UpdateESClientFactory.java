package com.unbank.es.update;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.unbank.common.constants.ServerConfig;

public class UpdateESClientFactory {
	private static Client client;
	
	private UpdateESClientFactory(){}
	
	public static Client getClient(){
		if(client==null){
			client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(ServerConfig.getServerUrl(), 9300));
		}
		return client;
	}
	
}
