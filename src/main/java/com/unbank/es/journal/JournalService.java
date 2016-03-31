package com.unbank.es.journal;

import static com.unbank.common.constants.CommonConstants.ERROR;
import static com.unbank.common.constants.CommonConstants.INDEX_JOURNAL;
import static com.unbank.common.constants.CommonConstants.PAGE_SIZE;
import static com.unbank.common.constants.CommonConstants.START_NUM;
import static com.unbank.common.constants.CommonConstants.SUCCESS;
import static com.unbank.common.constants.CommonConstants.TYPE_JOURNAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unbank.common.constants.CommonConstants;
import com.unbank.common.constants.SysParameters;
import com.unbank.common.entity.ResponseParam;
import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.ESUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.common.utils.SourcesUtils;

/**
 * 期刊索引操作
 *
 * @author LiuLei
 */
public class JournalService {
    private static final Logger logger = LoggerFactory.getLogger(JournalService.class);

    public String index(Journal journal) {
        Map<String, String> map = new HashMap<String, String>();
        Client client = ESUtils.getClient();
        try {
            // 查询索引库是否存在
            boolean exists = ESUtils.queryIndexExists(client, INDEX_JOURNAL);
            if (exists) {
                // 查询数据是否存在
                boolean flag = ESUtils.searchIndexExists(client, INDEX_JOURNAL, "journalId", String.valueOf(journal.getJournalId()));
                // 数据已经存在
                if (flag) {
                    CommonUtils.putMap(map, ERROR, "索引已经存在", null);
                    return GsonUtil.getJsonStringFromObject(map);
                }
            }
            // 设置ESID为null
            journal.setEsId(null);
            // 设置期刊更新时间为当前系统时间
            journal.setUpdateTime(System.currentTimeMillis());
            IndexResponse response = client.prepareIndex(INDEX_JOURNAL, TYPE_JOURNAL)
                    .setSource(journal.toJsonString())
                    .get();
            
            String esId = response.getId();
            
            logger.info("期刊库：添加期刊索引信息完成,index,end,journalId:" + journal.getJournalId() + ",\t status:" + CommonConstants.SUCCESS + ",\t ESID:" + esId);
            CommonUtils.putMap(map, SUCCESS, "添加索引成功", esId);
            return GsonUtil.getJsonStringFromObject(map);
        } catch (Exception e) {
        	logger.error("期刊库：添加期刊索引信息失败,index,end,journalId:" + journal.getJournalId() + ",\t status:" + CommonConstants.ERROR);
            logger.error(CommonUtils.getExceptionMessage(e));
            CommonUtils.putMap(map, ERROR, "添加索引信息失败", null);
            return GsonUtil.getJsonStringFromObject(map);
        }
    }

    public String update(Journal journal) {
        Map<String, String> map = new HashMap<String, String>();
        // 初始的创建时间不变
        journal.setCreateTime(null);
        Client client = ESUtils.getClient();
        try {
            UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate(INDEX_JOURNAL, TYPE_JOURNAL, journal.getEsId());
            // 防止生成esId字段
            journal.setEsId(null);
            updateRequestBuilder.setDoc(GsonUtil.getJsonStringFromObject(journal)).get();

            CommonUtils.putMap(map, SUCCESS, "修改索引成功", null);
            logger.info("期刊库：修改期刊索引信息,update,end,journalId:" + journal.getJournalId() + ",\t status:" + CommonConstants.SUCCESS);
            return GsonUtil.getJsonStringFromObject(map);

        } catch (Exception e) {
        	logger.error("期刊库：修改期刊索引信息,update,end,journalId:" + journal.getJournalId() + ",\t status:" + CommonConstants.ERROR);
            logger.error(CommonUtils.getExceptionMessage(e));
            CommonUtils.putMap(map, ERROR, "修改索引信息失败", null);
            return GsonUtil.getJsonStringFromObject(map);
        }
    }

    public String deleteByJournalId(int journalId) {
        Map<String, String> map = new HashMap<String, String>();
        Client client = ESUtils.getClient();
        try {
        	// 删除期刊文档DeleteByQueryResponse delDoc
        	client.prepareDeleteByQuery(SysParameters.DOCUMENTINDEX)
        			.setTypes(SysParameters.DOCUMENTTYPE)
        			.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("articleJournalId", journalId)))
        			.get();
        	
            client.prepareDeleteByQuery(INDEX_JOURNAL).setQuery(QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("journalId", journalId)))
                    .get();
            CommonUtils.putMap(map, SUCCESS, "删除索引成功", null);
            logger.info("期刊库：依据期刊ID删除索引,deleteByJournalId,end,journalId:" + journalId + ",\t status:" + CommonConstants.SUCCESS);
            return GsonUtil.getJsonStringFromObject(map);

        } catch (Exception e) {
        	logger.error("期刊库：依据期刊ID删除索引失败,deleteByJournalId,end,journalId:" + journalId + ",\t status:" + CommonConstants.ERROR);
            logger.error(CommonUtils.getExceptionMessage(e));
            CommonUtils.putMap(map, ERROR, "删除索引失败", null);
            return GsonUtil.getJsonStringFromObject(map);
        }
    }

    public String deleteByIds(List<String> journalIds) {
        Map<String, String> map = new HashMap<String, String>();
        Client client = ESUtils.getClient();
        try {
        	// 删除期刊文档
        	client.prepareDeleteByQuery(SysParameters.DOCUMENTINDEX)
        			.setTypes(SysParameters.DOCUMENTTYPE)
        			.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("articleJournalId", journalIds)))
        			.get();
        	 
        	// 删除期刊信息
            client.prepareDeleteByQuery(INDEX_JOURNAL)
            		.setTypes(TYPE_JOURNAL)
            		.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("journalId", journalIds)))
            		.get();
            
            CommonUtils.putMap(map, SUCCESS, "批量删除索引成功", null);
            return GsonUtil.getJsonStringFromObject(map);

        } catch (Exception e) {
            logger.error(CommonUtils.getExceptionMessage(e));
            CommonUtils.putMap(map, ERROR, "批量删除索引失败", null);
            return GsonUtil.getJsonStringFromObject(map);
        }
    }

    public String searchByJourlnalId(int journalId) {
        Map<String, String> map = new HashMap<String, String>();
        Client client = ESUtils.getClient();
        try {
            QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("journalId", journalId));

            SearchHits hits = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, queryBuilder, null, START_NUM, PAGE_SIZE);

            if (CommonUtils.isEmpty(hits) || hits.getTotalHits() == 0) {
                logger.info("依据期刊ID搜索查无结果,journalId:[" + journalId + "]");
                CommonUtils.putMap(map, ERROR, "依据期刊ID搜索查无结果", null);
                return GsonUtil.getJsonStringFromObject(map);
            }

            SearchHit hit = hits.getHits()[0];
            Map<String, Object> source = hit.getSource();

            Journal journalInfo = GsonUtil.getObjectFromJsonStr(GsonUtil.getJsonStringFromObject(source), Journal.class);

            journalInfo.setEsId(hit.getId());

            map.put("jsonData", GsonUtil.getJsonStringFromObject(journalInfo));

            CommonUtils.putMap(map, SUCCESS, "成功", null);
            return GsonUtil.getJsonStringFromObject(map);

        } catch (IndexMissingException e) {
            logger.error(CommonUtils.getExceptionMessage(e));
            CommonUtils.putMap(map, ERROR, "依据期刊ID搜索失败", null);
            return GsonUtil.getJsonStringFromObject(map);
        }
    }

    public String searchJournalBySearchData(List<JournalSearchData> searchData, int from, int pageSize, String orderByField, String order) {
    	if(pageSize == 0){
        	pageSize = CommonConstants.PAGE_SIZE;
        }
    	// 返回对象
        ResponseParam responseParam = new ResponseParam();
        Client client = ESUtils.getClient();
        try {
        	// 封装QueryBuilder
        	BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            SearchHits hits;
            
            // 调用自定义QueryBuilder条件
            for (JournalSearchData temp : searchData) {
            	queryBuilder.must(temp.getQueryBuilder(null));
			}
            
            // 排序方式 默认ES搜索排序
            List<FieldSortBuilder> sorts = ESUtils.getFieldSortBuilders(orderByField, order);
            
            hits = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, queryBuilder, sorts, from, pageSize);

            if (CommonUtils.isEmpty(hits) || hits.getTotalHits() == 0) {
            	responseParam.setStatus(ERROR);
            	responseParam.setMsg("查无结果");
                return GsonUtil.getJsonStringFromObject(responseParam);
            }

            List<Journal> result = new ArrayList<Journal>();

            for (SearchHit hit : hits.getHits()) {
                Map<String, Object> source = hit.getSource();

                Journal tempInfo = GsonUtil.getObjectFromJsonStr(GsonUtil.getJsonStringFromObject(source), Journal.class);
                tempInfo.setEsId(hit.getId());

                result.add(tempInfo);
            }

            responseParam.setStatus(SUCCESS);
            responseParam.setMsg("成功");
            responseParam.setJsonData(GsonUtil.getJsonStringFromObject(result));
            // 总数
            responseParam.setCount(hits.getTotalHits());
            
            return GsonUtil.getJsonStringFromObject(responseParam);

        } catch (IndexMissingException e) {
            logger.error(CommonUtils.getExceptionMessage(e));
            responseParam.setStatus(ERROR);
            responseParam.setMsg("搜索失败");
            return GsonUtil.getJsonStringFromObject(responseParam);
        }
    }

    public String searchByColumn(List<JournalSearchData> searchData, List<String> topList,
    		int from, int pageSize, String orderByField, String order) {
    	if(pageSize == 0){
        	pageSize = CommonConstants.PAGE_SIZE;
        }
        // 返回对象
        ResponseParam responseParam = new ResponseParam();
        // 记录总数
        long totalHits = 0;
        Client client = ESUtils.getClient();
        try {
        	// 排序方式 默认审核时间倒序
            List<FieldSortBuilder> sortBuilders = ESUtils.getFieldSortBuilders(orderByField, order);

            // 记录数据搜索数据
            List<Journal> journalList = new ArrayList<Journal>();
            // 查询数
            if(pageSize == 0){
            	pageSize = CommonConstants.PAGE_SIZE;
            }

            // 记录已经删除的期刊ID
            List<String> deleteList = null;
            for (JournalSearchData temp : searchData) {
            	deleteList = new ArrayList<String>();
            	if(CommonUtils.isNotEmpty(temp.getJournalIdNot())){
            		deleteList.addAll(temp.getJournalIdNot());
            	}
			}

            Map<String, String> deleteMap = new HashMap<String, String>();
            if(CommonUtils.isNotEmpty(searchData) && CommonUtils.isNotEmpty(deleteList)){
            	for (String jourId : deleteList) {
					deleteMap.put(jourId, jourId);
				}
            }

            // 无置顶信息
            if (CommonUtils.isEmpty(topList)) {
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                // 不为空，记录删除标记
                for (JournalSearchData temp : searchData) {
                	boolQueryBuilder.should(temp.getQueryBuilder(null));
                }

                // 如果没有置顶数据，则直接查询
                SearchHits hits = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, boolQueryBuilder, sortBuilders, from, pageSize);

//                System.out.println("总记录数：" + hits.getTotalHits());
                totalHits = hits.getTotalHits();

//                System.out.println("无置顶信息，共搜索：" + hits.getHits().length + "条数据！");
                for (SearchHit hit : hits.getHits()) {
                    Map<String, Object> source = hit.getSource();

                    Journal tempInfo = GsonUtil.getObjectFromJsonStr(GsonUtil.getJsonStringFromObject(source), Journal.class);
                    tempInfo.setEsId(hit.getId());
                    String tempKey = String.valueOf(tempInfo.getJournalId());
                    if(CommonUtils.isNotEmpty(deleteMap) && CommonUtils.isNotEmpty(deleteMap.get(tempKey))){
                    	// 标记为已删除
                    	tempInfo.setStatus(2);
                    }

                    journalList.add(tempInfo);
                }
            } else {
                int count = 0;
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                // 如果起始值小于置顶数据才进入本方法
                if (from < topList.size()) {
                    // 如果有置顶，先查询出所有的置顶数据
                    boolQueryBuilder.must(QueryBuilders.termsQuery("journalId", topList));

                    SearchHits hits = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, boolQueryBuilder, null, from, pageSize);

//                    System.out.println("置顶共搜索：" + hits.getHits().length + "条数据！");

                    Map<Integer, Journal> journalMap = new HashMap<Integer, Journal>();
                    for (SearchHit hit : hits.getHits()) {
                        Map<String, Object> source = hit.getSource();

                        Journal tempInfo = GsonUtil.getObjectFromJsonStr(GsonUtil.getJsonStringFromObject(source), Journal.class);
                        tempInfo.setEsId(hit.getId());
                        // 标记为置顶
                        tempInfo.setStatus(1);

                        journalMap.put(tempInfo.getJournalId(), tempInfo);
                    }

                    for (int i = from; i < journalMap.size(); i++) {
                        journalList.add(journalMap.get(Integer.parseInt(topList.get(i))));
                        count++;
                        if (count == pageSize) {
                            break;
                        }
                    }
                }

                // 除第一页外，获取上次补取数据的数量
                // 如第一页取了3条，总数要取20点，那么补取数据就是17
                if (from > 0 && pageSize > journalList.size()) {
                	from = from - topList.size();
                }
                // 如果查询到的数据不够一页，再查询 size - journalList.size() 条数据
                // 查询总数 - 置顶数 = 补充数
                pageSize = pageSize - count;

                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                // 加载搜索条件
                for (JournalSearchData temp : searchData) {
                	queryBuilder.should(temp.getQueryBuilder(null));
                }
                // 排除top信息
                queryBuilder.mustNot(QueryBuilders.termsQuery("journalId", topList));

                SearchHits hits = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, queryBuilder, sortBuilders, from, pageSize);

//                System.out.println("总记录数：" + (hits.getTotalHits() + count));
                totalHits = hits.getTotalHits() + topList.size();

//                if (journalList.size() > 0) {
//                    System.out.println("置顶（补数据）搜索：" + hits.getHits().length + "条数据！");
//                } else {
//                    System.out.println("共搜索：" + hits.getHits().length + "条数据！");
//                }
                for (SearchHit hit : hits.getHits()) {
                    Map<String, Object> source = hit.getSource();

                    Journal tempInfo = GsonUtil.getObjectFromJsonStr(GsonUtil.getJsonStringFromObject(source), Journal.class);
                    tempInfo.setEsId(hit.getId());
                    String tempKey = String.valueOf(tempInfo.getJournalId());
                    if(CommonUtils.isNotEmpty(deleteMap) && CommonUtils.isNotEmpty(deleteMap.get(tempKey))){
                    	// 标记为已删除
                    	tempInfo.setStatus(2);
                    }

                    journalList.add(tempInfo);
                }
            }

            // 封装返回值
            responseParam.setStatus(SUCCESS);
            responseParam.setMsg("成功");
            responseParam.setJsonData(GsonUtil.getJsonStringFromObject(journalList));
            responseParam.setCount(totalHits);
            return GsonUtil.getJsonStringFromObject(responseParam);

        } catch (IndexMissingException e) {
            logger.error(CommonUtils.getExceptionMessage(e));
            responseParam.setStatus(ERROR);
            responseParam.setMsg("搜索失败");
            return GsonUtil.getJsonStringFromObject(responseParam);
        }
    }


    public String searchByColumns(List<JournalSearchData> searchData, List<String> topList,
                                 int from, int pageSize, String orderByField, String order) {
        if(pageSize == 0){
            pageSize = CommonConstants.PAGE_SIZE;
        }
        // 返回对象
        ResponseParam responseParam = new ResponseParam();
        // 记录总数
        long totalHits = 0;
        Client client = ESUtils.getClient();
        try {
            // 排序方式 默认审核时间倒序
            List<FieldSortBuilder> sortBuilders = ESUtils.getFieldSortBuilders(orderByField, order);

            // 记录数据搜索数据
            List<Journal> journalList = new ArrayList<Journal>();

            // 记录已经删除的期刊ID
            List<String> deleteList = new ArrayList<String>();;
            for (JournalSearchData temp : searchData) {
                if(CommonUtils.isNotEmpty(temp.getJournalIdNot())){
                	deleteList.addAll(temp.getJournalIdNot());
                }
            }

            Map<String, String> deleteMap = new HashMap<String, String>();
            if(CommonUtils.isNotEmpty(searchData) && CommonUtils.isNotEmpty(deleteList)){
                for (String jourId : deleteList) {
                    deleteMap.put(jourId, jourId);
                }
            }
            
            if(CommonUtils.isEmpty(topList)){
            	BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            	
            	List<String> tempNotJournalIds = new ArrayList<String>();
                // 封装QueryBuilder
                for (JournalSearchData temp : searchData) {
                	if(CommonUtils.isEmpty(temp.getDeleteTag()) && CommonUtils.isNotEmpty(temp.getJournalIdNot())){
                		// 记录所有不显示的期刊
                		tempNotJournalIds.addAll(temp.getJournalIdNot());
                		// 构造Query时设为null
                		temp.setJournalIdNot(null);
                	}
                    boolQueryBuilder.should(temp.getQueryBuilder(null));
                }
                // 过滤掉标记为不显示的期刊信息
                FilteredQueryBuilder query = QueryBuilders.filteredQuery(boolQueryBuilder, FilterBuilders.notFilter(FilterBuilders.termsFilter("journalId", tempNotJournalIds)));

                // 如果没有置顶数据，则直接查询
                SearchHits hits = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, query, sortBuilders, from, pageSize);

                System.out.println("总记录数：" + hits.getTotalHits());
                totalHits = hits.getTotalHits();

                System.out.println("无置顶信息，共搜索：" + hits.getHits().length + "条数据！");
                for (SearchHit hit : hits.getHits()) {
                    Journal tempInfo = SourcesUtils.getJournalInfo(hit.getSource());
                    tempInfo.setEsId(hit.getId());
                    // 判断期刊ID是否在标记删除的MAP中
                    String tempKey = String.valueOf(tempInfo.getJournalId());
                    if(CommonUtils.isNotEmpty(deleteMap.get(tempKey))){
                        // 标记为已删除
                        tempInfo.setStatus(2);
                    }

                    journalList.add(tempInfo);
                }
            }else{
            	BoolQueryBuilder topsQueryBuilder = QueryBuilders.boolQuery();
            	
            	List<String> tempNotJournalIds = new ArrayList<String>();
                // 封装QueryBuilder
                for (JournalSearchData temp : searchData) {
                	if(CommonUtils.isEmpty(temp.getDeleteTag()) && CommonUtils.isNotEmpty(temp.getJournalIdNot())){
                		// 记录所有不显示的期刊
                		tempNotJournalIds.addAll(temp.getJournalIdNot());
                		// 构造Query时设为null
                		temp.setJournalIdNot(null);
                	}
                	topsQueryBuilder.should(temp.getQueryBuilder(topList));
                }
                
                // 过滤掉标记为不显示的期刊信息
                FilteredQueryBuilder query = QueryBuilders.filteredQuery(topsQueryBuilder, FilterBuilders.notFilter(FilterBuilders.termsFilter("journalId", tempNotJournalIds)));
                // 查询出符合条件的置顶数据
                SearchHits tops = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, query, sortBuilders, from, pageSize);

                long topTotal = tops.getTotalHits();
                
                // 包含top
                if(CommonUtils.isNotEmpty(tops) && tops.getTotalHits() > 0){
                	SearchHit[] searchHits = tops.getHits();
                	System.out.println("置顶共搜索：" + searchHits.length + "条数据！");

                	List<Journal> topResult = new ArrayList<Journal>();
                	
                	for (SearchHit searchHit : searchHits) {
                		Journal tempInfo = SourcesUtils.getJournalInfo(searchHit.getSource());
                		tempInfo.setEsId(searchHit.getId());
                		String tempKey = String.valueOf(tempInfo.getJournalId());
                        if(CommonUtils.isNotEmpty(deleteMap.get(tempKey))){
                            // 标记为已删除
                            tempInfo.setStatus(2);
                        }
                        // 标记为已购买
                        tempInfo.setBuyFlag(1);
                        topResult.add(tempInfo);
    				}
                	
                	int count = 0;
                    // 如果起始值小于置顶数据才进入本方法
                    if (CommonUtils.isNotEmpty(topResult)) {
                        for (int i = 0; i < topResult.size(); i++) {
                            journalList.add(topResult.get(i));
                            count++;
                            if (count == pageSize) {
                                break;
                            }
                        }
                    }

                    // 如果查询到的数据不够一页，再查询 size - journalList.size() 条数据
                    // 查询总数 - 置顶数 = 补充数
                    pageSize = pageSize - count;

                    if(pageSize > 0){
                    	// 除第一页外，获取上次补取数据的数量 3+17
                        // 如第一页取了3条，总数要取20点，那么补取数据就是17 from = 20-3
                        if (from > tops.getTotalHits()) {
                            from = from - (int)tops.getTotalHits();
                        }
                        
                    	BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                        // 加载搜索条件
                        for (JournalSearchData temp : searchData) {
                            queryBuilder.should(temp.getQueryBuilder(null));
                        }
                        // 排除top信息
                        queryBuilder.mustNot(QueryBuilders.termsQuery("journalId", topList));

                        SearchHits hits = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, queryBuilder, sortBuilders, from, pageSize);

                        System.out.println("总记录数：" + (hits.getTotalHits() + topTotal));
                        totalHits = hits.getTotalHits() + topTotal;

                        System.out.println("置顶（补数据）搜索：" + hits.getHits().length + "条数据！");
                        for (SearchHit hit : hits.getHits()) {
                            Journal tempInfo = SourcesUtils.getJournalInfo(hit.getSource());
                            tempInfo.setEsId(hit.getId());
                            String tempKey = String.valueOf(tempInfo.getJournalId());
                            if(CommonUtils.isNotEmpty(deleteMap.get(tempKey))){
                                // 标记为已删除
                                tempInfo.setStatus(2);
                            }

                            journalList.add(tempInfo);
                        }
                    } else {
                    	BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
                        // 加载搜索条件
                        for (JournalSearchData temp : searchData) {
                            queryBuilder.should(temp.getQueryBuilder(null));
                        }
                        // 排除top信息
                        queryBuilder.mustNot(QueryBuilders.termsQuery("journalId", topList));

                        SearchHits hits = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, queryBuilder, sortBuilders, from, pageSize);

                        System.out.println("总记录数：" + (hits.getTotalHits() + topTotal));
                        totalHits = hits.getTotalHits() + topTotal;
                    }
                } else {// 无置顶信息
                	
                	BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                    // 封装QueryBuilder
                    for (JournalSearchData temp : searchData) {
                        boolQueryBuilder.should(temp.getQueryBuilder(null));
                    }
                    // 排除top信息
                    boolQueryBuilder.mustNot(QueryBuilders.termsQuery("journalId", topList));

                    
                    if(topTotal >= pageSize){
                    	from = (int)topTotal- pageSize;
                    }
                    
                    // 过滤掉标记为不显示的期刊信息
                    FilteredQueryBuilder noTopQuery = QueryBuilders.filteredQuery(boolQueryBuilder, FilterBuilders.notFilter(FilterBuilders.termsFilter("journalId", tempNotJournalIds)));
                    
                    // 如果没有置顶数据，则直接查询
                    SearchHits hits = ESUtils.search(client, INDEX_JOURNAL, TYPE_JOURNAL, noTopQuery, sortBuilders, from, pageSize);

                    System.out.println("总记录数：" + (hits.getTotalHits() + topTotal));
                    totalHits = hits.getTotalHits() + topTotal;

                    System.out.println("无置顶信息，共搜索：" + hits.getHits().length + "条数据！");
                    for (SearchHit hit : hits.getHits()) {
                        Journal tempInfo = SourcesUtils.getJournalInfo(hit.getSource());
                        tempInfo.setEsId(hit.getId());
                        // 判断期刊ID是否在标记删除的MAP中
                        String tempKey = String.valueOf(tempInfo.getJournalId());
                        if(CommonUtils.isNotEmpty(deleteMap.get(tempKey))){
                            // 标记为已删除
                            tempInfo.setStatus(2);
                        }

                        journalList.add(tempInfo);
                    }
                }
            }
            
            // 封装返回值
            responseParam.setStatus(SUCCESS);
            responseParam.setMsg("成功");
            responseParam.setJsonData(GsonUtil.getJsonStringFromObject(journalList));
            responseParam.setCount(totalHits);
            return GsonUtil.getJsonStringFromObject(responseParam);

        } catch (IndexMissingException e) {
            logger.error(CommonUtils.getExceptionMessage(e));
            responseParam.setStatus(ERROR);
            responseParam.setMsg("搜索失败");
            return GsonUtil.getJsonStringFromObject(responseParam);
        }
    }
}
