package com.unbank.db.mybatis.test;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.unbank.db.mybatis.client.CrawlMapper;
import com.unbank.db.mybatis.vo.Crawl;
import com.unbank.db.mybatis.vo.CrawlExample;

public class Test {
	public static void main(String[] args) {
		CrawlMapper mapper=
				(CrawlMapper)new ClassPathXmlApplicationContext("applicationContext.xml").getBean("crawlMapper");
		
		CrawlExample  example=new CrawlExample();
		example.createCriteria().andWebsiteIdBetween(100, 200);
		List<Crawl> list= mapper.selectByExample(example);
		
		for(Crawl c:list){
			System.out.println(c.getCrawlTitle()+":"+c.getCrawlTime());
		}
		System.out.println(list.size());
		
	}
}
