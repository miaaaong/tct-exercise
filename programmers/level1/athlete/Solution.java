package programmers.level1.athlete;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

    public String solution1(String[] participant, String[] completion) {
    	String answer = "";
    	Arrays.sort(participant);
    	Arrays.sort(completion);
    	
    	for(int i=0; i<completion.length; i++) {
    		if(!completion[i].equals(participant[i])) {
    			answer = participant[i];
    			break;
    		}
    	}
    	if(answer.equals("")) {
    		answer = participant[participant.length-1];
    	}
    	
    	return answer;
    }
    
    public String solution2(String[] participant, String[] completion) {
    	String answer = "";
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	for(String name : completion) {
    		if(map.containsKey(name)) {
    			map.put(name, map.get(name)+1);
    		} else {
    			map.put(name, 1);
    		}
    	}
    	
    	for(String name : participant) {
    		if(map.containsKey(name)) {
    			int count = map.get(name);
    			if(count == 1) {
    				map.remove(name);
    			} else {
    				map.put(name, count-1);
    			}
    		} else {
    			answer = name;
    			break;
    		}
    	}
    	
    	return answer;
    }
}
