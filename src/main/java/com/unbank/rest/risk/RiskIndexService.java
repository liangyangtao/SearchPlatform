package com.unbank.rest.risk;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.unbank.common.constants.CommonConstants;
import com.unbank.common.utils.GsonUtil;
import com.unbank.entity.risk.Risk;
import com.unbank.es.risk.RiskIndexClient;
import com.unbank.es.update.UpdateClient;

@Component
@Path("/riskIndex")
public class RiskIndexService {
	
	private static final Logger logger = LoggerFactory.getLogger(RiskIndexService.class); 
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/passAndIndex")
	public String passAndIndex(@FormParam("riskJsonStr") String riskJsonStr,@FormParam("extraTag") String extraTag){
		// 返回数据
		Map<String, String> map = new HashMap<String, String>();

		RiskIndexClient indexClient = new RiskIndexClient();
		UpdateClient updateClient = new UpdateClient();

		Risk risk = GsonUtil.getObjectFromJsonStr(riskJsonStr, Risk.class);

		if (!risk.checkRisk()) {
			map.put("status", CommonConstants.ERROR);
			map.put("msg", "警告：标题、正文、crawl_id、categoryId都不能为空");
			return GsonUtil.getJsonStringFromObject(map);
		}
		
		int crawlId = risk.getCrawl_id();
		
		logger.info("建立风险库索引,passAndIndex,begin,crawl_id:" + crawlId);
		
		// LiuLei update 2015-12-03
		indexClient.index(map, risk);
		
		if(CommonConstants.SUCCESS.equals(map.get("status"))){
			boolean bl = updateClient.appendExtraTagById(String.valueOf(crawlId),extraTag);
			boolean bl2 = updateClient.appendTagsById(String.valueOf(crawlId), risk.getCategory());
			
			if(!bl || !bl2){
				logger.info("建立风险库索引,更新索引失败,passAndIndex,end,crawl_id:" + crawlId);
				map.put("status", CommonConstants.ERROR);
				map.put("msg", "更新索引失败");
			}
		}
		
		String json = GsonUtil.getJsonStringFromObject(map);
		return json;
	}
	
	
	
}
