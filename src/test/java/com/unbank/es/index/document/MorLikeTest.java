package com.unbank.es.index.document;

import com.unbank.es.document.DocumentSearchClient;

public class MorLikeTest {
	public static void main(String[] args) {
		DocumentSearchClient client = new DocumentSearchClient();
		client.searchMoreLike("webword_23732", 0, 8);
	}
}
