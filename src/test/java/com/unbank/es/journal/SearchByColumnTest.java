package com.unbank.es.journal;

import java.util.ArrayList;
import java.util.List;

public class SearchByColumnTest {
	public static void main(String[] args) {
		JournalService service = new JournalService();
		// 置顶
		List<String> topList = new ArrayList<String>();
		topList.add("15");
		topList.add("9");
		topList.add("11");
		
		List<JournalSearchData> searchDatas = new ArrayList<JournalSearchData>();
		
		
		
		String result = service.searchByColumn(searchDatas, topList, 20, 10, "passTime", "desc");
		
		System.out.println(result);
		
	}
}
