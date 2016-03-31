package com.unbank.es.search;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.unbank.common.constants.ServerConfig;

public class SearchESClientFactory {
	
	private static Client client;
	
	private SearchESClientFactory(){}
	
	public static Client getClient(){
		if(client==null){
			client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(ServerConfig.getServerUrl(), 9300));
		}
		return client;
	}
	
}
