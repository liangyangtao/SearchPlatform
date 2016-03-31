package com.unbank.rest.news;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.unbank.es.search.SearchErrorInfo;
import com.unbank.es.update.UpdateClient;

@Component
@Path("/updateNews")
public class UpdateNewsService {

	private static final Logger logger = LoggerFactory.getLogger(UpdateNewsService.class);

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/appendWebsiteIDAndTag")
	public String appendWebsiteIDAndTag(@FormParam("crawl_id") String crawl_id, @FormParam("tag") String tag,
			@FormParam("website_id") String website_id) {

		logger.info("(/updateNews/appendWebsiteIDAndTag),修改websiteID,crawl_id:" + crawl_id + ",website_id:" + website_id + ",tag:" + tag);

		/**
		 * 检验crawl_id和website_id是否是Integer类型数据
		 */
		try {
			Integer.valueOf(crawl_id);
			Integer.valueOf(website_id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(SearchErrorInfo.ILLEGALARGS);
			return SearchErrorInfo.ILLEGALARGS;
		}
		UpdateClient uc = new UpdateClient();

		boolean bl = uc.appendWebsiteIDAndTag(crawl_id, website_id, tag);
		return bl == true ? "OK" : "ERROR";
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/appendTagById")
	public String appendTagById(@FormParam("crawl_id") String crawl_id, @FormParam("tag") String tag) {

		logger.info("appendTagById,crawl_id:" + crawl_id + ",tag:" + tag);
		/**
		 * 检验crawl_id和website_id是否是Integer类型数据
		 */
		try {
			Integer.valueOf(crawl_id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(SearchErrorInfo.ILLEGALARGS);
			return SearchErrorInfo.ILLEGALARGS;
		}
		UpdateClient uc = new UpdateClient();
		boolean bl = uc.appendTagsById(crawl_id, tag);
		return bl == true ? "OK" : "ERROR";
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/appendExtraTagById")
	public String appendExtraTagById(@FormParam("crawl_id") String crawl_id, @FormParam("extraTag") String extraTag){
		logger.info("appendExtraTagById,crawl_id:" + crawl_id + ",extraTag:" + extraTag);
		try {
			Integer.valueOf(crawl_id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(SearchErrorInfo.ILLEGALARGS);
			return SearchErrorInfo.ILLEGALARGS;
		}
		UpdateClient uc = new UpdateClient();
		boolean bl = uc.appendExtraTagById(crawl_id, extraTag);
		return bl == true ? "OK" : "ERROR";
	}
	
}
