package com.unbank.rest.news;

import static com.unbank.common.constants.CommonConstants.ERROR;
import static com.unbank.common.constants.CommonConstants.PARAMERROR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.es.news.NewsService;

@Component
@Path("/newsDelete")
public class NewsController {
	
	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
	
	@Autowired
	private NewsService newsService;
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/delete")
	public String delete(@FormParam("ids") String ids) {
		
		Map<String, String> map = new HashMap<String, String>();
		// 参数判断
        if (CommonUtils.isEmpty(ids)) {
            logger.error("新闻库：删除新闻,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
        }
        logger.info("新闻库：批量删除索引:" + ids);
        // 转换json数据
        List<String> newsList = GsonUtil.getListFromJson(ids);
        
		return newsService.delete(newsList);
	}
}
