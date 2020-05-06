package programmers.level3.foldingpaper;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    public int[] solution(int n) {
        int[] answer = {};
        List<Integer> list = get(n, null);
        int size = (int) (Math.pow(2, n))-1;
        answer = new int[size];
        for(int i=0; i<answer.length; i++) {
        	answer[i] = list.get(i);
        }
        
        return answer;
    }
    
    private List<Integer> get(int n, List<Integer> input) {
    	if(n == 1) {
    		List<Integer> list = new ArrayList<Integer>();
    		list.add(0);
    		return list;
    	} else {
    		List<Integer> list = get(n-1, input);
    		list.add(0);
    		for(int i=list.size()-2; i>=0; i--) {
    			list.add(1-list.get(i));
    		}
    		return list;
    	}
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Solution solution = new Solution();
		int[] result = solution.solution(4);
		for(int val : result)
		System.out.print(val);
		System.out.print(",\n");
	}

}
