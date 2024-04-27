package com.lgcns.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lgcns.test.state.StateInfo;

public class Workflow {

	private String startFrom;
	private Map<String, StateInfo> state = new HashMap<String, StateInfo>();
	private List<String> responses = new ArrayList<String>();

	public String getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(String startFrom) {
		this.startFrom = startFrom;
	}

	public Map<String, StateInfo> getState() {
		return state;
	}

	public void setState(Map<String, StateInfo> state) {
		this.state = state;
	}

	public List<String> getResponses() {
		return responses;
	}

	public void setResponses(List<String> responses) {
		this.responses = responses;
	}

	public void run() {
		StateInfo stateInfo = state.get(startFrom);
		while(true) {
			try {
				String nextStateName = stateInfo.run();
				if(nextStateName.equals("end")) {
					break;
				} else {
					stateInfo = state.get(nextStateName);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
