package com.lgcns.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lgcns.test.state.ActionStateInfo;
import com.lgcns.test.state.Choice;
import com.lgcns.test.state.ChoiceStateInfo;
import com.lgcns.test.state.ParallelStateInfo;
import com.lgcns.test.state.StateInfo;

public class InfoParser {

	public static ActionStateInfo getActionStateInfo(String stateName, JsonObject stateInfo) {
		String stateType = stateInfo.get("type").getAsString();
		String stateUrl = stateInfo.get("url").getAsString();
		JsonArray paramArray = stateInfo.get("parameters").getAsJsonArray();
		List<String> params = new ArrayList<String>();
		for (int i = 0; i < paramArray.size(); i++) {
			String paramName = paramArray.get(i).getAsString();
			params.add(paramName);
		}
		String nextStateName = stateInfo.get("next").getAsString();
		
		ActionStateInfo stateInfoObj = new ActionStateInfo();
		stateInfoObj.setStateName(stateName);
		stateInfoObj.setStateType(stateType);
		stateInfoObj.setStateUrl(stateUrl);
		stateInfoObj.setStateParameters(params);
		stateInfoObj.setNextStateName(nextStateName);
		
		return stateInfoObj;
	}
	
	public static ParallelStateInfo getParallelStateInfo(String stateName, JsonObject stateInfo) {
		String stateType = stateInfo.get("type").getAsString();
		String nextStateName = stateInfo.get("next").getAsString();
		List<Workflow> branches = new ArrayList<Workflow>();
		JsonArray jsonArray = stateInfo.get("branches").getAsJsonArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject workflowInfo = jsonArray.get(i).getAsJsonObject();
			branches.add(getWorkflow(workflowInfo));
		}
		ParallelStateInfo stateInfoObj = new ParallelStateInfo();
		stateInfoObj.setStateName(stateName);
		stateInfoObj.setStateType(stateType);
		stateInfoObj.setBranches(branches);
		stateInfoObj.setNextStateName(nextStateName);
		
		return stateInfoObj;
	}
	
	public static ChoiceStateInfo getChoiceStateInfo(String stateName, JsonObject stateInfo) {
		String stateType = stateInfo.get("type").getAsString();
		String nextStateName = stateInfo.get("next").getAsString();
		List<Choice> choices = new ArrayList<Choice>();
		JsonArray jsonArray = stateInfo.get("choices").getAsJsonArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject choiceObj = jsonArray.get(i).getAsJsonObject();
			Choice choice = new Choice();
			choice.setVariable(choiceObj.get("variable").getAsString());
			choice.setEqual(choiceObj.get("equal").getAsString());
			choice.setNext(choiceObj.get("next").getAsString());
			choices.add(choice);
		}
		
		ChoiceStateInfo choiceStateInfoObj = new ChoiceStateInfo();
		choiceStateInfoObj.setStateName(stateName);
		choiceStateInfoObj.setStateType(stateType);
		choiceStateInfoObj.setChoices(choices);
		choiceStateInfoObj.setNextStateName(nextStateName);
		
		return choiceStateInfoObj;
	}
	
	public static Workflow getWorkflow(JsonObject workflowInfo) {
		String startFrom = workflowInfo.get("startFrom").getAsString();
		
		// responses
		JsonArray responseArray = workflowInfo.get("responses").getAsJsonArray();
		List<String> responses = new ArrayList<String>();
		for (int i = 0; i < responseArray.size(); i++) {
			String responseName = responseArray.get(i).getAsString();
			responses.add(responseName);
		}
		
		// state
		Map<String, StateInfo> state = new HashMap<String, StateInfo>();
		JsonObject statesObject = workflowInfo.get("state").getAsJsonObject();
		Set<Entry<String, JsonElement>> entrySet = statesObject.entrySet();
		Iterator<Entry<String, JsonElement>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, JsonElement> next = iterator.next();
			String stateName = next.getKey();
			JsonObject stateInfo = next.getValue().getAsJsonObject();
			String stateType = stateInfo.get("type").getAsString();
			if(stateType.equals("action")) {
				ActionStateInfo actionStateInfo = getActionStateInfo(stateName, stateInfo);
				state.put(stateName, actionStateInfo);
			} else if(stateType.equals("parallel")) {
				ParallelStateInfo parallelStateInfo = getParallelStateInfo(stateName, stateInfo);
				state.put(stateName, parallelStateInfo);
			} else if(stateType.equals("choice")) {
				ChoiceStateInfo choiceStateInfo = getChoiceStateInfo(stateName, stateInfo);
				state.put(stateName, choiceStateInfo);
			}
		}
		
		Workflow workflow = new Workflow();
		workflow.setStartFrom(startFrom);
		workflow.setResponses(responses);
		workflow.setState(state);
		return workflow;
	}
}
