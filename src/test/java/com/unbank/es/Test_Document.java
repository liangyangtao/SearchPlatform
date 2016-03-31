package com.unbank.es;

import java.io.IOException;

import org.elasticsearch.action.mlt.MoreLikeThisRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbank.entity.SearchNews;
import com.unbank.es.document.Document;
import com.unbank.es.document.DocumentIndexClient;
import com.unbank.es.search.SearchESClientFactory;

public class Test_Document {
	
//	@Test
	public void test_index(){
		DocumentIndexClient client=new DocumentIndexClient();
		Document document=new Document();
		document.setArticleBrief("在一系列利好政策消息下，7月1日，沪指下跌223.52点，两市800只个股跌停。7月1日晚，" +
				"证监会连夜发布三大重磅利好消息，A股交易结算收费标准下调；允许所有证券公司发行与转让证券公司短期公司债券；" +
				"券商自主决定强制平仓线，不再设6个月的强制还款。据新京报记者统计，这是证监会4个交易日来第6次“关切”股市。");

		document.setArticleContent("7月1日，证监会例行发布会上，新闻发言人张晓军便给市场吃了颗定心丸。张晓军称，市场虽然在短期内出现较大幅度的回调，但经济企稳向好的趋势更加明显。这也是本轮调整以来，证监会首次官方回应市场运行情况。6月29日，证监会官方微博发布中国证券金融公司答记者问，两融风险总体可控强制平仓的规模很小。当天沪指仍以3.34%的跌幅收盘。当日晚10点，证监会又发布了“答记者问”，称近期股市的下跌是前期过快上涨的调整。6月30日，证监会官微又转发证券业协会答记者问，称券商可向符合条件的第三方提供接口。当日，沪指绝地反弹，大涨5.53%收盘。"+
"券商可自主决定平仓线"+"监管层频繁出面也未能让沪指在昨日止跌。昨日晚间，证监会连夜发布三大重磅利好消息。"+
"沪、深交易所和中国证券登记结算公司拟调降A股交易结算相关收费标准，8月起实施。其中，沪、深证券交易所收取的A股交易经手费由按成交金额0.0696‰双边收取，调整为按成交金额0.0487‰双边收取，降幅为30%。同时，中国证券登记结算公司收取的A股交易过户费由目前沪市按照成交面值0.3‰双向收取、深市按照成交金额0.0255‰双向收取，一律调整为按照成交金额0.02‰双向收取，降幅约为33%。"+
"同时出台的两融新规规定，允许券商在客户信用基础上，和客户商定展期次数；取消投资者维持担保比例低于130%应当在2个交易日内追加担保物的规定，允许券商与客户自行商定补充担保物的期限与比例。"+
"民生证券首席策略分析师李少君分析认为，以实际行动维护市场稳定的部门陆续增加，这不是第一个，相信也不会是最后一个。");
		document.setArticleCover("封面");
		document.setArticleId(1232);
		document.setArticleInfo("证监会连续发声救市 券商可自主决定平仓线");
		document.setArticleKeyWord("证监会1 平仓 A股 券商 交易");
		document.setArticleLabel("股票1 财经2");
		document.setArticleName("证监会连续发声救市 券商可自主决定平仓线");
		document.setArticlePrice(1.7f);
		document.setArticleProject("webword");
		document.setArticleSave("upload");
		document.setArticleSkip("券商可自主决定平仓线");
		document.setArticleType("document");
		document.setArticleUser("user1");
		document.setDocId(234);
		document.setDocPath("d:/234.docx");
		document.setDownTime(1432026900000l);
		document.setHtmlName("1253332.html");
		document.setHtmlPage("20");
		document.setInsertTime(1432026900000l);
		document.setPassTime(1432226900000l);
		document.setPassType("saved");
		document.setPassUser(111);
		document.setPassUserName("梁擎宇");
		document.setUpdateTime(1432126900000l);
		document.setUserId(122);
		System.out.println(document.toJsonString());
		System.out.println(document.getArticleContent().length());

//		for(int i=0;i<10;i++){
//			document.setArticleId(100+i);
//			String id= client.index(document);
//			System.out.println(id);
//		}
//		System.out.println(client.index(document));
		//AU5MZU_bPjWlmgW-7S-8
	}
	
	//@Test
	public void test_morelike(){
		Client cilent=SearchESClientFactory.getClient();
		
		
		MoreLikeThisRequestBuilder builder = new MoreLikeThisRequestBuilder( cilent, "unbanknews", "news",
				"21594311");
		
		builder.setField("content");// 匹配的字段
		
		builder.setSearchFrom(0).setSearchSize(15);
		long time1=System.currentTimeMillis();
		SearchResponse response =  cilent.moreLikeThis(builder.request()).actionGet();
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits();
		long time2=System.currentTimeMillis();
		
		System.out.println(time2-time1);
		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			ObjectMapper mapper = new ObjectMapper();
			try {
				SearchNews news = mapper.readValue(json, SearchNews.class);
				System.out.println(hit.getScore()+":"+news.getTitle());
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
}
