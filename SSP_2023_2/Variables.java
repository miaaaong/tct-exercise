package com.lgcns.test;

import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Variables {

	private static Map<String, String> variables = new HashMap<String, String>();
	
	static {
		String filePath = "./VARIABLE.JSON";
		JsonObject jsonObject = null;
		try(FileReader fr = new FileReader(filePath);) {
			jsonObject = JsonParser.parseReader(fr).getAsJsonObject();
			Set<Entry<String,JsonElement>> entrySet = jsonObject.entrySet();
			Iterator<Entry<String, JsonElement>> iterator = entrySet.iterator();
			while(iterator.hasNext()) {
				Entry<String, JsonElement> next = iterator.next();
				String name = next.getKey();
				String value = next.getValue().getAsString();
				variables.put(name, value);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static String getQueryString(List<String> varList) {
		StringBuffer sb = new StringBuffer();
		if(varList == null || varList.size() == 0) {
			return sb.toString();
		}
		sb.append("?");
		for(int i=0; i<varList.size(); i++) {
			String varName = varList.get(i);
			String varValue = variables.get(varName);
			sb.append(varName);
			sb.append("=");
			try {
				sb.append(URLEncoder.encode(varValue, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(i < (varList.size()-1)) {
				sb.append("&");
			}
		}
		return sb.toString();
	}
	
	public static void updateVariable(String key, String value) {
		variables.put(key, value);
	}
	
	public static String getValue(String key) {
		return variables.get(key);
	}
}
