package programmers.demo_a.fullarray;

import java.util.Arrays;

class Solution {
	public boolean solution(int[] arr) {
		boolean answer = true;

		Arrays.sort(arr);
		for(int i=0; i<arr.length; i++) {
			if(arr[i] != (i+1)) {
				answer = false;
				break;
			}
		}
		
		return answer;
	}
}