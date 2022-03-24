package combination;

import java.util.*;

public class Solution {
	
	// card에서 1~n개를 뽑는 방법
	public List<Integer> solution(int[] card) {
		boolean[] visitor = new boolean[card.length];
		List<Integer> list = new ArrayList<Integer>();
		for(int i=1; i<=card.length; i++) {
			combination(card, 0, i, visitor, list);
		}
		return list;
	}
	
	public void combination(int[] card, int start, int r, boolean[] visitor, List<Integer> list) {
		if(r == 0) {
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<visitor.length; i++) {
				if(visitor[i]) {
					sb.append(String.valueOf(card[i]));
				}
			}
			int result = Integer.parseInt(sb.toString());
			list.add(result);
			return;
		}
		
		for(int i=start; i<card.length; i++) {
			visitor[i] = true;
			combination(card, i+1, r-1, visitor, list);
			visitor[i] = false;
		}
	}
	

	public static void main(String[] args) {
		Solution solution = new Solution();
		int[] card = {1,2,3,4};
		List<Integer> list = solution.solution(card);
		for(int value : list) {
			System.out.println(value);
		}
	}

}
