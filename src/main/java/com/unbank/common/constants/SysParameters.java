package com.unbank.common.constants;

public class SysParameters {

	/**
	 * 索引名称
	 */
	public final static String INDEXNAME = "unbanknews";

	/**
	 * 索引类型
	 */
	public final static String DATATYPE = "news";

	/**
	 * 图数据库服务器地址
	 */
	public final static String NEO4JHOST = "http://10.0.2.147:8080";

	/**
	 * 风险库名称
	 */
	public final static String RISKINDEX = "unbankrisk";

	/**
	 * 风险类型
	 */
	public final static String RISKTYPE = "risk";

	/**
	 * 文档库索引名称
	 */
	public final static String DOCUMENTINDEX = "unbankdocument";

	/**
	 * 文档库索引类型
	 */
	public final static String DOCUMENTTYPE = "document";

	/**
	 * 依据标签名称搜索其子标签
	 */
	public final static String GETCHILDTAGBYNAMEURL = NEO4JHOST
			+ "/synchronous/getWordsCategoryAndAlias.htm";

	public final static String CHECKTAGSBYKEYWORDSURL = NEO4JHOST
			+ "/synchronous/getAllRelatedTags.htm";

	public final static String ORDERBYTIMENAME = "newsDate";

	public final static int CONTENTLENGHT = 100;

	public final static float TITLESTRSEARCHBOOST = 3f;

	public final static float CONTENTSTRSEARCHBOOST = 3f;

	public final static float MINSCORE = 1.25f;

	public final static float STRINGQUERYMINSCORE = 0.1f;

}
