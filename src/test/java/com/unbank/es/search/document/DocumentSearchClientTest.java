package com.unbank.es.search.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unbank.common.utils.CommonUtils;
import com.unbank.common.utils.GsonUtil;
import com.unbank.es.journal.JournalParam;
import com.unbank.es.journal.JournalSearchData;

/**
 * Created by LiuLei on 2015/11/4.
 */
public class DocumentSearchClientTest { // extends TestCase
	public static void main(String[] args) {
		
		JournalSearchData searchInfo = new JournalSearchData();
		
		List<String> journalIdMust = new ArrayList<String>();
		for (int i = 1; i < 10000; i++) {
			journalIdMust.add(String.valueOf(i));
		}
		
		searchInfo.setJournalIdMust(journalIdMust);
		searchInfo.setShowData("1");
		
		List<JournalSearchData> searchData = new ArrayList<JournalSearchData>();
		searchData.add(searchInfo);
		
		JournalParam param = new JournalParam();
		param.setFrom(0);
		param.setPageSize(100000);
		param.setJsonData(searchData);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("searchParamJson", GsonUtil.getJsonStringFromObject(param));
		
		String url = "http://localhost:8080/SearchPlatform/rest/journal/searchJournalByQueryString";
		String result = CommonUtils.getHtml(map, url);
		
		System.out.println(result);
	}

}