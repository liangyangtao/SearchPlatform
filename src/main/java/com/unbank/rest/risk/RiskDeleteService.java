package com.unbank.rest.risk;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.entity.News;
import com.unbank.entity.risk.SearchRisk;
import com.unbank.es.risk.RiskDeleteClient;
import com.unbank.es.risk.RiskSearchClient;
import com.unbank.es.search.SearchClient;
import com.unbank.es.search.SearchErrorInfo;
import com.unbank.es.update.UpdateESClientFactory;

@Component
@Path("/riskDelete")
public class RiskDeleteService {
	
	private static final Logger logger = LoggerFactory.getLogger(RiskDeleteService.class); 
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/delete")
	public String delete(@FormParam("id") String id, @FormParam("extraTag") String extraTag){
		logger.info("Service 风险库：依据ID删除（单篇）相应的索引，根据crawl_id 关联更新新闻库，id:"+id+"，extraTag:"+extraTag);
		
		if(CommonUtils.isEmpty(id)||CommonUtils.isEmpty(extraTag)){
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		int crawl_id;
		try {
			//根据参数risk.id找到news.crawl_id
			RiskSearchClient searchClient = new RiskSearchClient();
			SearchRisk risk = searchClient.searchByESId(id);
			crawl_id = risk.getCrawl_id();
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return "风险库内容不存在：id="+id;
		}
		logger.info("Service 风险库：crawl_id:"+crawl_id);
		
		try {
			//根据参数news.crawl_id找到news.extraTag
			SearchClient searchClientNews = new SearchClient();
			News news = searchClientNews.SearchByCrawlId(crawl_id);
			String extraTagStr = news.getExtraTag();
			
			if (extraTagStr!=null){
				//extraTagStr = extraTagStr + " risk-FLFX-pass risk-FLFX1-pass";//test 末尾情况
				//extraTagStr = "risk-FLFX-pass " + extraTagStr + " risk-FLFX1-pass";//test 两端情况
				logger.info("news.extraTagStr:"+extraTagStr);
				//替换指定字符串为空，同时去掉空格
				List<String> extraTagParaList=new ArrayList<String>();
				Gson gson = new Gson();
				extraTagParaList = gson.fromJson(extraTag, new TypeToken<List<String>>() {  }.getType());
				for (String s : extraTagParaList) {
					extraTagStr = extraTagStr.replace(s, "");
					logger.info("replace:"+s+"为空");
				}
				extraTagStr=extraTagStr.trim().replaceAll(" +", " ");
			}
			logger.info("准备写入news.extraTagStr:"+extraTagStr);
				
			UpdateRequestBuilder updateRequestBuilder = UpdateESClientFactory.getClient().prepareUpdate(
					SysParameters.INDEXNAME, SysParameters.DATATYPE, String.valueOf(crawl_id));
			updateRequestBuilder.setDoc("extraTag", extraTagStr);
			updateRequestBuilder.execute().actionGet();
			logger.info("新闻库更新成功");
		} catch (JsonSyntaxException e2) {
			logger.error(e2.getMessage());
			return "新闻库内容不存在：crawl_id="+crawl_id;
		}
		
		try {
			//删除风险库文章
			RiskDeleteClient client = new RiskDeleteClient();
			return client.delete(id)==true?"SUCCESS":"FALSE";
		} catch (Exception e3) {
			logger.error(e3.getMessage());
			return "删除风险库文章失败";
		}
	}
	
	
/*	public String delete(@FormParam("ids") String ids){
		logger.info("Service 文档库：依据ID删除相应的索引，id:"+ids);
		
		if(StringUtils.isBlank(ids)){
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		RiskDeleteClient client=new RiskDeleteClient();
		List<String> idList=new ArrayList<String>();
		Gson gson=new Gson();
		idList= gson.fromJson(ids, new TypeToken<List<String>>() {  }.getType());
		return client.delete(idList);
	}*/
}
