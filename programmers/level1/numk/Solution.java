package programmers.level1.numk;

import java.util.Arrays;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
    public static int[] solution(int[] array, int[][] commands) {
        int[] answer = new int[commands.length];
        for(int rotation=0; rotation<commands.length; rotation++) {
        	int[] input = commands[rotation];
        	int i = input[0];
        	int j = input[1];
        	int k = input[2];
        	
        	int[] sub = new int[j-i+1];
        	int pos = 0;
        	for(int idx=i-1; idx<j; idx++) {
        		sub[pos] = array[idx];
        		pos++;
        	}
        	Arrays.sort(sub);
        	
        	answer[rotation] = sub[k-1];
        }
        return answer;
    }

}
