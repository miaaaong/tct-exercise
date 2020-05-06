package programmers.level2.development;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] progresses = {93,30,55};
		int[] speeds = {1,30,5};
		int[] result = solution(progresses, speeds);
		for(int i : result) {
			System.out.println(i);
		}
	}

    public static int[] solution(int[] progresses, int[] speeds) {
    	int[] days = new int[progresses.length];
    	for(int i=0; i<progresses.length; i++) {
    		int target = (100 - progresses[i])/speeds[i];
    		int remain = (100 - progresses[i])%speeds[i];
    		if(remain > 0) {
    			target++;
    		}
    		days[i] = target;
    	}
    	
    	for(int i=1; i<days.length; i++) {
    		if(days[i] < days[i-1]) {
    			days[i] = days[i-1];
    		}
    	}
    	
    	Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
    	for(int day : days) {
    		if(map.containsKey(day)) {
    			map.put(day, map.get(day)+1);
    		} else {
    			map.put(day, 1);
    		}
    	}
    	
        int[] answer = new int[map.size()];
        int idx = 0;
        Iterator<Entry<Integer, Integer>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()) {
        	answer[idx] = iterator.next().getValue();
        	idx++;
        }
        return answer;
    }

}
