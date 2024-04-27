package com.lgcns.test.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lgcns.test.Variables;

public class ActionStateInfo extends StateInfo {

	private String stateUrl;
	private List<String> stateParameters = new ArrayList<String>();

	public String getStateUrl() {
		return stateUrl;
	}

	public void setStateUrl(String stateUrl) {
		this.stateUrl = stateUrl;
	}

	public List<String> getStateParameters() {
		return stateParameters;
	}

	public void setStateParameters(List<String> stateParameters) {
		this.stateParameters = stateParameters;
	}

	@Override
	public String run() throws Exception {
		String queryString = Variables.getQueryString(stateParameters);
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		ContentResponse contentResponse = httpClient.newRequest(stateUrl + queryString)
				.method(HttpMethod.GET).send();
		String actionResponse = contentResponse.getContentAsString();
		httpClient.stop();			
	
		// result
		JsonObject jsonObject = JsonParser.parseString(actionResponse).getAsJsonObject();
		Set<Entry<String,JsonElement>> entrySet = jsonObject.entrySet();
		Iterator<Entry<String, JsonElement>> iterator = entrySet.iterator();
		while(iterator.hasNext()) {
			Entry<String, JsonElement> next = iterator.next();
			String name = next.getKey();
			String value = next.getValue().getAsString();
			Variables.updateVariable(name, value);
		}
	
		return this.getNextStateName();
	}

}
