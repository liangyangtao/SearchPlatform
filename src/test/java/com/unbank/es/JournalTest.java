package com.unbank.es;

import com.unbank.es.journal.Journal;
import com.unbank.es.journal.JournalService;

public class JournalTest {

	public static void main(String[] args) {
		JournalService journalService = new JournalService();
		Journal info = new Journal();
		info.setName("中国银行管理中心");
		info.setSkip("时光飞逝");
		info.setLabel("银行人");
		info.setKeyWord("银行 期刊");
		info.setCover("http://10.0.2.235");
		info.setType("月刊");
		info.setPassType("SUBMITTED");
		info.setSubmitUserId(49);
		info.setSubmitUserName(null);
		info.setSubmitTime(System.currentTimeMillis());
		info.setCreateTime(System.currentTimeMillis());
		info.setPassTime(System.currentTimeMillis());
		info.setHaveData(1);
		info.setPrice(0);

		for (int i = 10001; i < 50000; i++) {
			info.setJournalId(i + 1);
			info.setName("1多标签测试" + i);
			info.setLabel("互联网金融日报 商业银行现金管理年度分析报告 投资银行业务动态周刊");
			info.setPassType("SAVED");
			info.setHaveData(1);
			System.out.println(journalService.index(info));
		}
	}
}
