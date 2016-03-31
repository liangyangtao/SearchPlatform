package com.unbank.es;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.unbank.entity.NewsSearchCondition;

public class test {
	public static void main(String[] args) throws IOException {
		// 武器 31035615
		// 瑞雯 31026887
		// GetRequestBuilder ss =
		// ESUtils.getClient().prepareGet(SysParameters.INDEXNAME,
		// SysParameters.DATATYPE, '31053238');
		// GetResponse result = ss.get();
		// if(result.isExists()){
		// Map<String, Object> source = result.getSource();
		//
		// System.out.println(source.get('content').toString());

		// String esid = source.get('crawl_id').toString();
		// System.out.println(esid);

		// TransportClient client =
		// ESUtils.getClusterClient(CommonConstants.CLUSTERNAME_DEFAULT, true,
		// 9300, '10.0.2.152');
		//
		// IndexRequestBuilder indexRequestBuilder =
		// client.prepareIndex(SysParameters.INDEXNAME, 'news', esid);
		// indexRequestBuilder.setSource(result.getSourceAsString());
		// indexRequestBuilder.execute().actionGet();
		// }
		String jsonStr = "";
		Gson gson = new Gson();
		NewsSearchCondition newsSearchCondition = gson.fromJson(jsonStr,
				NewsSearchCondition.class);
//		newsSearchCondition.getMustContentWords();
		List<String> mustWordNames = new ArrayList<String>();
		mustWordNames.add("A");
		mustWordNames.add("B");
		mustWordNames.add("E");
		List<String> shouldWordNames = new ArrayList<String>();
		shouldWordNames.add("C");
		shouldWordNames.add("D");
		shouldWordNames.add("F");
		shouldWordNames.add("H");
		List<String> result = new ArrayList<String>();
		for (String stringA : mustWordNames) {
			for (int i = 0; i < shouldWordNames.size(); i++) {
				result.add(stringA + " " + shouldWordNames.get(i));
				StringBuffer temp = new StringBuffer();
				temp .append(shouldWordNames.get(i));
				for (int j = i+1; j < shouldWordNames.size(); j++) {
					temp.append( " "+ shouldWordNames.get(j));
					result.add(stringA + " " + temp.toString());
				}
			}

		}
		for (String string : result) {
			System.out.println(string);
		}

	}
}
