package com.unbank.rest.document;

import static com.unbank.common.constants.CommonConstants.ERROR;
import static com.unbank.common.constants.CommonConstants.PARAMERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unbank.common.constants.CommonConstants;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.ESUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.es.document.Document;
import com.unbank.es.document.DocumentDeleteClient;
import com.unbank.es.document.DocumentIndexClient;
import com.unbank.es.document.DocumentUpdateClient;
import com.unbank.es.search.SearchErrorInfo;

@Component
@Path("")
public class DocumentController {

	private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/documentDelete/delete")
	public String delete(@FormParam("ids") String ids){
		logger.info("Service 文档库：依据ID删除相应的索引,id:"+ids);
		
		if(CommonUtils.isEmpty(ids)){
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		DocumentDeleteClient client=new DocumentDeleteClient();
		List<String> idList=new ArrayList<String>();
		Gson gson=new Gson();
		idList= gson.fromJson(ids, new TypeToken<List<String>>() {  }.getType());
		return client.delete(idList);
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/documentIndex/index")
	public String index(@FormParam("documentJsonStr") String documentJsonStr){
		DocumentIndexClient indexClient=new DocumentIndexClient();
		Gson gson=new Gson();
		Document document= gson.fromJson(documentJsonStr, Document.class);
		
		if(document.getArticleId()==0||document.getArticleProject()==null||document.getArticleContent()==null){
			return SearchErrorInfo.ILLEGALARGS;
		}
		
		logger.info("建立文档库索引,index,begin,articleId:"+document.getArticleId());
		String result= indexClient.index(document);
		return result==null?"ERROR":result;
	}

	/**
	 * 添加包含期刊ID的文档索引
	 * @param jsonData
	 * @return
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/documentIndex/indexByJournalId")
	public String indexByJournalId(@FormParam("documentJsonStr") String jsonData) {
		// 记录返回数据
		Map<String, String> map = new HashMap<String, String>();
		// 判断传入的参数不请允许为空
		if(CommonUtils.isEmpty(jsonData)){
			logger.error("添加包含期刊ID的文档索引,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
		}
		// 转换json数据为Document类
		Document doc = GsonUtil.getObjectFromJsonStr(jsonData, Document.class);
		
		if (doc.getArticleId() == 0 || CommonUtils.isEmpty(doc.getArticleProject()) 
				|| CommonUtils.isEmpty(doc.getArticleContent())
				|| doc.getArticleJournalId() == 0) {
			
			logger.error("添加包含期刊ID的文档索引,------" + PARAMERROR);
            CommonUtils.putMap(map, ERROR, PARAMERROR, null);
            return GsonUtil.getJsonStringFromObject(map);
		}
		
		DocumentIndexClient indexClient = new DocumentIndexClient();
		
		logger.info("添加包含期刊ID的文档索引,index,begin,articleId:" + doc.getArticleId());
		
		return indexClient.indexByJournalId(doc, map);
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/documentUpdate/update")
	public String update(@FormParam("documentJsonStr") String documentJsonStr) {
		Document document = GsonUtil.getObjectFromJsonStr(documentJsonStr, Document.class);

		if (CommonUtils.isEmpty(document.getArticleContent())) {
			return "文档内容不能为空";
		}
		logger.info("更新文档库索引,index,begin,articleId:" + ESUtils.getIdOfLucene(document));
		// 调用接口
		DocumentUpdateClient client = new DocumentUpdateClient();
		boolean bl = client.update(document);
		if(bl){
			logger.info("更新文档库索引,index,end,articleId:" + ESUtils.getIdOfLucene(document)  + ",\t status:" + CommonConstants.SUCCESS);
			return "SUCCESS";
		}else{
			logger.error("更新文档库索引,index,end,articleId:" + ESUtils.getIdOfLucene(document) + ",\t status:" + CommonConstants.ERROR);
			return "ERROR";
		}
	}

	/**
	 * 修改文档索引（含期刊信息）
	 * 注：功能同上,只是对于返回值不一样;------------------------暂无使用
	 * @param documentJsonStr
	 * @return
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/documentUpdate/updateAboutJournal")
	public String updateAboutJournal(@FormParam("documentJsonStr") String documentJsonStr) {
		// 记录返回数据
		Map<String, String> map = new HashMap<String, String>();
		// 判断传入的参数不请允许为空
		if(CommonUtils.isEmpty(documentJsonStr)){
			logger.error("修改文档索引（含期刊信息）,------" + PARAMERROR);
			CommonUtils.putMap(map, ERROR, PARAMERROR, null);
			return GsonUtil.getJsonStringFromObject(map);
		}

		Document document = GsonUtil.getObjectFromJsonStr(documentJsonStr, Document.class);

		if (CommonUtils.isEmpty(document.getArticleContent())) {
			CommonUtils.putMap(map, ERROR, "文档内容不能为空", null);
			return GsonUtil.getJsonStringFromObject(map);
		}

		logger.info("修改文档索引（含期刊信息）,index,begin,articleId:" + ESUtils.getIdOfLucene(document));

		// 调用接口
		DocumentUpdateClient client = new DocumentUpdateClient();
		boolean bl = client.update(document);
		// 判断执行结果
		if (!bl) {
			logger.error("修改文档索引（含期刊信息）,------失败");
			CommonUtils.putMap(map, ERROR, "失败", null);
			return GsonUtil.getJsonStringFromObject(map);
		}

		CommonUtils.putMap(map, CommonConstants.SUCCESS, "成功", null);
		return GsonUtil.getJsonStringFromObject(map);
	}
}
