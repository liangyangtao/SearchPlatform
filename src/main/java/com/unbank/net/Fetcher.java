package com.unbank.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
public class Fetcher {
	
	private static final Logger logger = LoggerFactory.getLogger(Fetcher.class); 
	
	public String fetch(Map<String,String> arg,String url){
		logger.info("fetch开始，参数："+arg+"	url:"+url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		
		for(String key:arg.keySet()){
			nvps.add(new BasicNameValuePair(key, arg.get(key)));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			
			HttpResponse response = httpclient.execute(httpost);
			
			HttpEntity entity = response.getEntity();
			String s= EntityUtils.toString(entity);
			return s;
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	
	public HttpClient getHttpClient(){
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();  
		// Increase max total connection to 200  
		cm.setMaxTotal(200);  
		// Increase default max connection per route to 20  
		cm.setDefaultMaxPerRoute(20);  
		// Increase max connections for localhost:80 to 50  
		HttpHost host = new HttpHost("10.0.2.25", 8080);  
		cm.setMaxPerRoute(new HttpRoute(host), 50);  
		
		CloseableHttpClient httpClient = HttpClients.custom()
		        .setConnectionManager(cm)  
		        .build();  
		
		return httpClient;
	}
}
