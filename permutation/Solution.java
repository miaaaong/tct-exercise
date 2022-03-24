package permutation;

import java.util.*;

public class Solution {
	
	// card에서 1~n개를 뽑아 만들 수 있는 모든 숫자
	public List<Integer> solution(int[] card) {
		List<Integer> list = new ArrayList<Integer>();
		for(int i=1; i<=card.length; i++) {
			boolean[] visitor = new boolean[card.length];
			permutation(card, 0, i, visitor, new int[i], list);			
		}
		return list;
    }
	
	public void permutation(int[] card, int depth, int r, boolean[] visitor, int[] value, List<Integer> list) {
		if(depth == r) {
			StringBuilder sb = new StringBuilder();
			for(int val : value) {
				sb.append(val);
			}
			int result = Integer.parseInt(sb.toString());
			if(!list.contains(result)) {
				list.add(result);
			}
			return;
		}
		
		for(int i=0; i<card.length; i++) {
			if(!visitor[i]) {
				value[depth] = card[i];
				visitor[i] = true;
				permutation(card, depth+1, r, visitor, value, list);
				visitor[i] = false;
			}
		}
	}
	
	public static void main(String[] args) {
        Solution sol = new Solution();
        int[] card = {1,2,3,4};
		List<Integer> list = sol.solution(card);
		for(int value : list) {
			System.out.println(value);
		}
    }

}
