package com.unbank.rest.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.unbank.common.DBContextHolder;
import com.unbank.common.utils.GsonUtil;
import com.unbank.db.mybatis.dao.website.WebsiteMapper;
import com.unbank.db.mybatis.vo.BaseWebsiteInfo;
import com.unbank.db.mybatis.vo.WebsiteExample;
import com.unbank.db.mybatis.vo.WebsiteExample.Criteria;
import com.unbank.entity.SearchWebsites;
import com.unbank.entity.website.WebsiteParam;
import com.unbank.es.search.SearchErrorInfo;

@Component
@Path("/DBService")
public class DBService {
	
	@Autowired  
	private WebsiteMapper websiteMapper;
	 
	/**
	 * 依据网站名称和板块名称模糊匹配相应的网址
	 * @param webName
	 * @param sectionName
	 * @return
	 */
	@Path("/getWebsites")
	@POST
	public String getWebsites(@FormParam("web_name") String web_name,
			@FormParam("section_name")String section_name,
			@FormParam("pageSize")Integer pageSize,
			@FormParam("from")Integer from
			){
		WebsiteExample example=new WebsiteExample();
		Criteria criteria= example.createCriteria();
		criteria.andIstaskEqualTo(2);
		
		if(web_name!=null){
			criteria.andWebNameLike("%"+web_name+"%");
		}
		
		if(section_name!=null){
			criteria.andSectionNameLike("%"+section_name+"%");
		}
		if(from!=null&&pageSize!=null){
			example.setLimitStart(from);
			example.setLimitEnd(pageSize);
		}
		
		// 切换数据源
		DBContextHolder.setDBType("ubk_platform");
		List<BaseWebsiteInfo> baseInfo=	websiteMapper.selectBaseInfoByExample(example);
		int count= websiteMapper.countBaseInfoByExample(example);
		DBContextHolder.clearDBType();
		
		SearchWebsites sw=new SearchWebsites(count,baseInfo);
		
		if(baseInfo!=null){
			Gson gson=new Gson();
			return gson.toJson(sw);
		}
		return null;
	}
	
	@Path("/getWebsitesByWebNames")
	@POST
	public String getWebsitesByWebNames(@FormParam("webNames") String webNames){
		if(webNames==null){
			return SearchErrorInfo.ILLEGALARGS;
		}
		String[] names=webNames.split("_");
		List<String> nameList=new ArrayList<String>();
		for(String name:names){
			try {
				nameList.add(name);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		// 切换数据源
		DBContextHolder.setDBType("ubk_platform");
		List<BaseWebsiteInfo> baseInfo=	websiteMapper.selectBaseInfoByWebNames(nameList);
		DBContextHolder.clearDBType();
		
		if(baseInfo!=null){
			Gson gson=new Gson();
			return gson.toJson(baseInfo);
		}
		return null;
	}
	
	@Path("/getWebsitesByIds")
	@POST
	public String getWebsitesByIds(@FormParam("websiteIds") String websiteIds){
		if(websiteIds==null){
			return SearchErrorInfo.ILLEGALARGS;
		}
		String[] ids=websiteIds.split("_");
		List<Integer> idList=new ArrayList<Integer>();
		for(String id:ids){
			try {
				Integer iid=Integer.valueOf(id);
				idList.add(iid);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		// 切换数据源
		DBContextHolder.setDBType("ubk_platform");
		List<BaseWebsiteInfo> baseInfo=	websiteMapper.selectBaseInfoByIds(idList);
		DBContextHolder.clearDBType();
		
		if(baseInfo!=null){
			Gson gson=new Gson();
			return gson.toJson(baseInfo);
		}
		return null;
	}
	
	/**
	 * 分页查询资源网站名称
	 * @param webName
	 * @param from
	 * @param size
	 * @return
	 */
	@Path("/searchByWebName")
	@POST
	public String searchByWebName(@FormParam("webName") String webName, 
			@FormParam("from") int from, @FormParam("size") int size) {
		// 切换数据源
		DBContextHolder.setDBType("ubk_platform");
		WebsiteParam param = new WebsiteParam();
		// 查询总数
		int count = websiteMapper.searchCountByWebName(webName);
		if (count > 0) {
			// 查询分页数据
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("webName", webName);
			map.put("from", from);
			map.put("size", size);
			List<String> result = websiteMapper.searchByWebName(map);
			
			param.setData(result);
		}
		param.setCount(count);
		
		DBContextHolder.clearDBType();
		
		return GsonUtil.getJsonStringFromObject(param);
	}
}