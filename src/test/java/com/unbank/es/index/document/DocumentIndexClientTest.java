package com.unbank.es.index.document;

import java.io.IOException;

import org.elasticsearch.client.Client;

import com.unbank.common.utils.ESUtils;

/**
 * Created by Administrator on 2015/11/3.
 */
public class DocumentIndexClientTest {// extends TestCase 

    public static void main(String[] args) throws IOException {
    	
    	Client client = ESUtils.getClient();
    	
    	
    	
    	
    	
//    	DocumentIndexClient client = new DocumentIndexClient();
//
//        Document document = new Document();
//        for (int i = 0; i < 15; i++) {
//        	document.setArticleBrief("在一系列利好政策消息下，7月" + (i + 1) + "日，沪指下跌223.52点，两市800只个股跌停。7月1日晚，" +
//                    "证监会连夜发布三大重磅利好消息，A股交易结算收费标准下调；允许所有证券公司发行与转让证券公司短期公司债券；" +
//                    "券商自主决定强制平仓线，不再设6个月的强制还款。据新京报记者统计，这是证监会4个交易日来第6次“关切”股市。");
//        	
//        	document.setArticleContent("7月" + (i + 1) + "日，证监会例行发布会上，新闻发言人张晓军便给市场吃了颗定心丸。张晓军称，市场虽然在短期内出现较大幅度的回调，但经济企稳向好的趋势更加明显。这也是本轮调整以来，证监会首次官方回应市场运行情况。6月29日，证监会官方微博发布中国证券金融公司答记者问，两融风险总体可控强制平仓的规模很小。当天沪指仍以3.34%的跌幅收盘。当日晚10点，证监会又发布了“答记者问”，称近期股市的下跌是前期过快上涨的调整。6月30日，证监会官微又转发证券业协会答记者问，称券商可向符合条件的第三方提供接口。当日，沪指绝地反弹，大涨5.53%收盘。" +
//                    "券商可自主决定平仓线" + "监管层频繁出面也未能让沪指在昨日止跌。昨日晚间，证监会连夜发布三大重磅利好消息。" +
//                    "沪、深交易所和中国证券登记结算公司拟调降A股交易结算相关收费标准，8月起实施。其中，沪、深证券交易所收取的A股交易经手费由按成交金额0.0696‰双边收取，调整为按成交金额0.0487‰双边收取，降幅为30%。同时，中国证券登记结算公司收取的A股交易过户费由目前沪市按照成交面值0.3‰双向收取、深市按照成交金额0.0255‰双向收取，一律调整为按照成交金额0.02‰双向收取，降幅约为33%。" +
//                    "同时出台的两融新规规定，允许券商在客户信用基础上，和客户商定展期次数；取消投资者维持担保比例低于130%应当在2个交易日内追加担保物的规定，允许券商与客户自行商定补充担保物的期限与比例。" +
//                    "民生证券首席策略分析师李少君分析认为，以实际行动维护市场稳定的部门陆续增加，这不是第一个，相信也不会是最后一个。");
//        	
//            document.setArticleCover("封面");
//            document.setArticleId(i + 1);
//            document.setArticleInfo("证监会连续发声救市 券商可自主决定平仓线");
//            document.setArticleKeyWord("证监会1 平仓 A股 券商 交易");
//            document.setArticleLabel("股票1 财经2");
//            document.setArticleName("证监会连续发声救市 券商可自主决定");
//            document.setArticlePrice(1.7f);
//            document.setArticleProject("webword");
//            document.setArticleSave("upload");
//            document.setArticleSkip("券商可自主决定平仓线");
//            document.setArticleType("document");
//            document.setArticleUser("user1");
//            document.setDocId(234);
//            document.setDocPath("d:/234.docx");
//            document.setDownTime(1432026900000l);
//            document.setHtmlName("1253332.html");
//            document.setHtmlPage("20");
//            document.setInsertTime(1432026900000l);
//            document.setPassTime(1432226900000l);
//            document.setPassType("saved");
//            document.setPassUser(111);
//            document.setPassUserName("梁擎宇");
//            document.setUpdateTime(1432126900000l);
//            document.setUserId(122);
//            
//            document.setArticleJournalId(5);
//
//            Map<String, String> map = new HashMap<String, String>();
//            
//            document.setArticleFormat("doc");
//            
//            System.out.println(client.indexByJournalId(document, map));
//		}
        
	}
}