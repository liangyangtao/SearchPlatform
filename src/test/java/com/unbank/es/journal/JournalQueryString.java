package com.unbank.es.journal;

import java.util.ArrayList;
import java.util.List;

public class JournalQueryString {
	public static void main(String[] args) {
		JournalParam journalParam = new JournalParam();
		journalParam.setFrom(0);
		journalParam.setPageSize(20);
		journalParam.setOrderByField("updateTime");
		journalParam.setOrder("desc");
		journalParam.setFullContent(false);
		
		
		List<JournalSearchData> list = new ArrayList<JournalSearchData>();
		JournalSearchData searchData = new JournalSearchData();
		searchData.setShowData("1");
		list.add(searchData);
		
		JournalService journalService = new JournalService();
		
		journalService.searchJournalBySearchData(list, journalParam.getFrom(), journalParam.getPageSize(), 
				journalParam.getOrderByField(), journalParam.getOrder());
		
	}
}
