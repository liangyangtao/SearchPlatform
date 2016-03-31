package com.unbank.es.ckhtml;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

public class ckhtml {
	public static void main(String[] args) throws IOException {
		
		String str = FileUtils.readFileToString(new File("E:/1111.html"), "UTF-8");
		
		String result = Jsoup.parse(str).body().text();
		
		System.out.println(result);
	}
}
