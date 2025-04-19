package com.lgcns.test;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonClassTest {

	public static void main(String[] args) {
		
//		{
//			"state": 
//			{
//				"create": {
//			      "type": "action",
//			      "url": "http://127.0.0.1:8011/create",
//			      "parameters": [ "id" ]
//			    },
//			    "add": {
//			      "type": "action",
//			      "url": "http://127.0.0.1:8012/add",
//			      "parameters": [ "id", "key", "data" ]
//			    },
//			    "fetch": {
//			      "type": "action",
//			      "url": "http://127.0.0.1:8013/fetch",
//			      "parameters": []
//			    }
//			}
//		}		
		
		String filePath = "./STATE.JSON"; 
		
		JsonObject jsonObject = null;
		try(FileReader fr = new FileReader(filePath);) {
			jsonObject = JsonParser.parseReader(fr).getAsJsonObject();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Map<String, StateInfo> stateInfoMap = new HashMap<String, StateInfo>();
		
		Gson gson = new Gson();
		JsonObject stateInfos = jsonObject.get("state").getAsJsonObject();
		Set<Entry<String,JsonElement>> entrySet = stateInfos.entrySet();
		Iterator<Entry<String, JsonElement>> iterator = entrySet.iterator();
		
		while(iterator.hasNext()) {
			Entry<String, JsonElement> next = iterator.next();
			String stateName = next.getKey();
			JsonObject stateInfoObject = next.getValue().getAsJsonObject();
			StateInfo stateInfo = gson.fromJson(stateInfoObject, StateInfo.class);
			stateInfoMap.put(stateName, stateInfo);
		}
		
		System.out.println(stateInfoMap);
	}
	
}
