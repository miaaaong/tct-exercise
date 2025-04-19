package com.lgcns.test;

import java.util.ArrayList;
import java.util.List;

public class StateInfo {

	private String type;
	private String url;
	private List<String> parameters = new ArrayList<String>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

}
