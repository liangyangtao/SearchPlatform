package com.unbank.rest.others;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.unbank.common.constants.SysParameters;
import com.unbank.es.search.SearchErrorInfo;
import com.unbank.net.Fetcher;

@Component
@Path("/basicInfoSearch")
public class BasicInfoSearchService {

	private static final Logger logger = LoggerFactory.getLogger(BasicInfoSearchService.class); 
	
	@Path("/getChildTag")
	@GET
	public String getChildTagsByName(@QueryParam("tagName") String tagName){
		if (tagName == null) {
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		logger.info("getChildTagsByName,tagName:"+tagName);
		
		Fetcher fetcher = new Fetcher();
		Map<String, String> map = new HashMap<String, String>();
		map.put("words", tagName);
		
		String neo4JData = fetcher.fetch(map, SysParameters.GETCHILDTAGBYNAMEURL);
		
		return neo4JData;
	}
	
}
