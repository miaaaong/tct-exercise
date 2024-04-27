package com.lgcns.test.state;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.test.Variables;

public class ChoiceStateInfo extends StateInfo {

	private List<Choice> choices = new ArrayList<Choice>();

	public List<Choice> getChoices() {
		return choices;
	}

	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}

	@Override
	public String run() throws Exception {
		for(Choice choice : choices) {
			String value = Variables.getValue(choice.getVariable());
			if(value.equals(choice.getEqual())) {
				return choice.getNext();
			} else {
				// ���� �Ҹ��� �� ���� choice Ȯ��
				continue;
			}
		}
		return this.getNextStateName();
	}
	
}
