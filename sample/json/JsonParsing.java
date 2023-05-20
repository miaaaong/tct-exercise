package com.lgcns.test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonParsing {

	// json value
//	{
//	  "port": 5001,
//	  "routes": [
//	    {
//	      "pathPrefix": "/front",
//	      "url": "http://127.0.0.1:8081"
//	    },
//	    {
//	      "pathPrefix": "/auth",
//	      "url": "http://127.0.0.1:5002"
//	    }
//	  ]
//	}

	public static void main(String[] args) {
		// 1) load from file -> json object
		String filePath = "Proxy-1.json"; //args[0];
		
		JsonObject jsonObject = null;
		try(FileReader fr = new FileReader(filePath);) {
			jsonObject = JsonParser.parseReader(fr).getAsJsonObject();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// 2-1) string -> json object (top: object)
		String strJson = "";
		jsonObject = JsonParser.parseString(strJson).getAsJsonObject();
		
		// 2-2) string -> json object (top: list)
		String routesConfig = "";
		JsonElement jsonElements = JsonParser.parseString(routesConfig);
		JsonArray jsonArray = jsonElements.getAsJsonArray();
		for(int i=0; i<jsonArray.size(); i++) {
			JsonObject route = jsonArray.get(i).getAsJsonObject();
			System.out.println(route.get("pathPrefix").getAsString());
			System.out.println(route.get("url").getAsString());
		}
		
		// 3) get values of json object
		JsonElement portElement = jsonObject.get("port");
		int port = portElement.getAsInt();
		System.out.println("port => " + port);
		JsonElement routesElement = jsonObject.get("routes");

		Map<String,String> routes = new HashMap<String, String>();
		JsonArray routesArray = routesElement.getAsJsonArray();
		for(int i=0; i<routesArray.size(); i++) {
			JsonObject route = routesArray.get(i).getAsJsonObject();
			routes.put(route.get("pathPrefix").getAsString(), route.get("url").getAsString());
		}
		System.out.println("routes => " + routes);
		
		// output
//		port => 5001
//		routes => {/auth=http://127.0.0.1:5002, /front=http://127.0.0.1:8081}
		
		
		// 4) string -> json object by Gson
		String responseBody = "";
		Gson gson = new Gson();
		JsonObject jsonObj = gson.fromJson(responseBody, JsonObject.class);
		long timestamp = jsonObj.get("timestamp").getAsLong();
		String value = jsonObj.get("value").getAsString();
		JsonArray inputURIs = jsonObj.get("inputQueueURIs").getAsJsonArray();
		

		// 5) values -> json object -> string by Gson
		List<String> result = new ArrayList<String>();
		jsonArray = new JsonArray();
		for(String output : result) {
			jsonArray.add(output);
		}
		jsonObj = new JsonObject();
		jsonObj.add("result", jsonArray);
		String resultJsonStr = gson.toJson(jsonObj);
		

	}

}
