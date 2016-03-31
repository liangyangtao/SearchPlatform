package com.unbank.rest.news;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.unbank.common.constants.SysParameters;
import com.unbank.common.utils.CommonUtils;
import com.unbank.es.search.SearchErrorInfo;
import com.unbank.net.Fetcher;

@Component
@Path("/tagRelation")
public class TagRelationService {
	private static final Logger logger = LoggerFactory.getLogger(TagRelationService.class); 
	@Path("/getTagsByKeyWords")
	@POST
	public String getTagsByKeyWords(@FormParam("keywords") String keywords,
			@FormParam("tagNames") String tagNames) {
		
		logger.info("(/tagRelation/getTagsByKeyWords),keywords:" + keywords + ",tagNames:" + tagNames);
		
		if(CommonUtils.isEmpty(keywords)){
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		Fetcher fetcher=new Fetcher();
		Map<String,String> map=new HashMap<String,String>();
		if(!CommonUtils.isEmpty(tagNames)){
			keywords=keywords+"_"+tagNames;
		}
		map.put("words", keywords);
		
		String tags= fetcher.fetch(map, SysParameters.CHECKTAGSBYKEYWORDSURL);
		return tags;
	}
	
}
