package com.unbank.common.utils;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Gson工具类
 * @author LL
 *
 */
public class GsonUtil {
	static Gson gson = new Gson();
	
	/**
	 * 转换对象为json
	 * 
	 * @param obj
	 * @return
	 */
	public static String getJsonStringFromObject(Object obj) {
		return gson.toJson(obj);
	}
	
	/**
	 * 转换Json字符串为对象
	 * 
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> T getObjectFromJsonStr(String json, Class<T> classOfT) {

		return gson.fromJson(json, classOfT);
	}

	public static List<String> getListFromJson(String json) {
		return gson.fromJson(json, new TypeToken<List<String>>() {
		}.getType());
	}

}
