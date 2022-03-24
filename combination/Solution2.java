package combination;

import java.util.ArrayList;
import java.util.List;

public class Solution2 {
	
	public List<Integer> solution(int[] arr) {
		List<Integer> answer = new ArrayList<Integer>();
		for(int i=1; i<=arr.length; i++) {
			boolean[] visitor = new boolean[arr.length];
			combination(arr, 0, i, visitor, answer);
		}
		return answer;
	}

	public void combination(int[] arr, int start, int r, boolean[] visitor, List<Integer> list) {
		if(r == 0) {
			//result
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<visitor.length; i++) {
				if(visitor[i]) {
					sb.append(arr[i]);
				}
			}
			list.add(Integer.parseInt(sb.toString()));
//			System.out.println(sb.toString());
			return;
		}
		
		for(int i=start; i<arr.length; i++) {
			visitor[i] = true;
			combination(arr, i+1, r-1, visitor, list);
			visitor[i] = false;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Solution2 sol = new Solution2();
		int[] arr = {2,4,5,8};
		List<Integer> solution = sol.solution(arr);
		for(int val : solution) {
			System.out.println(val);
		}
	}

}
