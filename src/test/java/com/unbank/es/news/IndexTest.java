package com.unbank.es.news;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unbank.entity.News;

/**
 * 测试新闻添加对于重复的数据做标识
 */
public class IndexTest {
	public static void main(String[] args) {
		NewsService service = new NewsService();
		
//		List<String> ids = new ArrayList<String>();
//		
//		ids.add("1");
//		ids.add("2");
//		ids.add("3");
//		ids.add("4");
//		ids.add("7");
//		ids.add("10");
//		
//		service.delete(ids);
		
//		News news = new News();
//		news.setContent("这是测试内容而已 ");
//		SearchData data = service.searchMoreLikeByNewsInfo(news, 0, 5, 2, 1);
//		
//		System.out.println(data.getCount());
		
		
		
		List<News> newsList = new ArrayList<News>();
		News news = null;
		
		for (int i = 0; i < 15; i++) {
			news = new News();
			
			int index = i + 1;
			news.setCrawl_id(index);
			news.setUrl("http://www.baidu.com");
			news.setTitle("工行这只是一个测试标题" + index);
			news.setContent("这是测试内容而已 " + index);
			news.setNewsDate(new Date().getTime());
			news.setCrawlDate(new Date().getTime());
			news.setWebName("网站名称");
			news.setWebSectionName("板块名称");
			news.setWebsite_id("111 222");
			news.setTagName("银行 宏观");
			news.setP_url("http://www.baidu.com");
			news.setKeyWords("关键词/词2/ci");
			news.setExtraTag("tag1 tag2");

//			service.index(news);
			newsList.add(news);
		}
		Map<String, String> map = new HashMap<String, String>();
		service.bulkIndex(newsList, map);
	}
}
