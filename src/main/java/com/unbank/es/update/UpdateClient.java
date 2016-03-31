package com.unbank.es.update;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.entity.News;

public class UpdateClient {
	private static final Logger logger = LoggerFactory.getLogger(UpdateClient.class);

	/**
	 * 修改指定文档的websiteID属性： 在文档重复的情况下，只保存一篇文档，但需要记录与之重复的信息来源，方便按照信息来源搜索
	 * 
	 * @param crawl_id
	 * @param website_id
	 * 与此篇文档重复的信息来源（website_id）
	 * @return
	 */
	public boolean appendWebsiteIDAndTag(String crawl_id, String website_id, String tags) {
		logger.info("修改website_id,crawl_id:" + crawl_id + "	website_id:" + website_id + " tag:" + tags);
		News originalNews = SearchByCrawlId(crawl_id);

		if (originalNews == null) {
			return false;
		}
		logger.info("crawl_id:" + crawl_id + " 原来的tag:" + originalNews.getTagName());
		if (tags != null) {
			originalNews.appendTag(tags);
		}
		try {
			UpdateRequestBuilder updateRequestBuilder = UpdateESClientFactory.getClient().prepareUpdate(
					SysParameters.INDEXNAME, SysParameters.DATATYPE, crawl_id);
			Map<String, String> map = new HashMap<String, String>();
			map.put("website_id", originalNews.getWebsite_id() + " " + website_id);
			map.put("tagName", originalNews.getTagName());

			updateRequestBuilder.setDoc(map);
			// updateRequestBuilder.setDoc("website_id",originalNews.getWebsite_id()+
			// " "+website_id);
			// updateRequestBuilder.setDoc("tagName",originalNews.getTagName());
			updateRequestBuilder.execute().actionGet();
		} catch (Exception e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			return false;
		}
		logger.info("crawl_id:" + crawl_id + " 修改后的tag:" + originalNews.getTagName());
		return true;
	}

	public boolean appendTagsById(String crawl_id, String tags) {
		logger.info("appendeTagById,crawl_id:" + crawl_id + "	 tag:" + tags);
		News originalNews = SearchByCrawlId(crawl_id);
		logger.info("crawl_id:" + crawl_id + " 原来的tag:" + originalNews.getTagName());
		if (tags != null) {
			originalNews.appendTag(tags);
		}

		try {
			UpdateRequestBuilder updateRequestBuilder = UpdateESClientFactory.getClient().prepareUpdate(
					SysParameters.INDEXNAME, SysParameters.DATATYPE, crawl_id);
			updateRequestBuilder.setDoc("tagName", originalNews.getTagName());
			updateRequestBuilder.execute().actionGet();
		} catch (Exception e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			return false;
		}
		logger.info("crawl_id:" + crawl_id + " 修改后的tag:" + originalNews.getTagName());
		return true;
	}

	public boolean appendExtraTagById(String crawl_id, String extraTag) {
		logger.info("appendExtraTagById,crawl_id:" + crawl_id + "	 extraTag:" + extraTag);
		News originalNews = SearchByCrawlId(crawl_id);
		logger.info("crawl_id:" + crawl_id + " 原来的extraTag:" + originalNews.getExtraTag());

		if (extraTag != null) {
			originalNews.appendExtraTag(extraTag);
		}

		try {
			UpdateRequestBuilder updateRequestBuilder = UpdateESClientFactory.getClient().prepareUpdate(
					SysParameters.INDEXNAME, SysParameters.DATATYPE, crawl_id);

			updateRequestBuilder.setDoc("extraTag", originalNews.getExtraTag());
			updateRequestBuilder.execute().actionGet();
		} catch (Exception e) {
			logger.error(CommonUtils.getExceptionMessage(e));
			return false;
		}
		logger.info("crawl_id:" + crawl_id + " 修改后的extraTag:" + originalNews.getExtraTag());
		return true;
	}

	/**
	 * 按照单个ID查询
	 * 
	 * @param crawl_id
	 * @return
	 */
	private News SearchByCrawlId(String crawl_id) {
		GetRequestBuilder getRequestBuilder = UpdateESClientFactory.getClient().prepareGet(SysParameters.INDEXNAME,
				"news", crawl_id);
		GetResponse response = getRequestBuilder.execute().actionGet();
		String s = response.getSourceAsString();

		ObjectMapper mapper = new ObjectMapper();
		try {
			News news = mapper.readValue(s, News.class);
			return news;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
