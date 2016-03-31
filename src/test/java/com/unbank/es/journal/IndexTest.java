package com.unbank.es.journal;

public class IndexTest {

	public static void main(String[] args) {
		
		JournalService service = new JournalService();
		
		Journal journal = new Journal();
		journal.setJournalId(39);
		
		System.out.println(service.index(journal));
		
	}
}
