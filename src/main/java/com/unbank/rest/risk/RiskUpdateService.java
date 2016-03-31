package com.unbank.rest.risk;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.unbank.common.utils.CommonUtils;
import com.unbank.entity.risk.Risk;
import com.unbank.es.risk.RiskUpdateClient;


@Component
@Path("/riskUpdate")
public class RiskUpdateService {
	
	
private static final Logger logger = LoggerFactory.getLogger(RiskUpdateService.class); 
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/update")
	public String  update(@FormParam("id") String id, @FormParam("riskJsonStr") String riskJsonStr){
		RiskUpdateClient client=new RiskUpdateClient();
		Gson gson=new Gson();
		Risk risk= gson.fromJson(riskJsonStr, Risk.class);
		
		if(CommonUtils.isEmpty(risk.getContent())){
			return "文档内容不能为空";
		}
		
		
		logger.info("更新文档库索引，index，Id:"+id);
		boolean bl=client.update(id, risk);
		return bl?"SUCCESS":"ERROR";
	}

}
