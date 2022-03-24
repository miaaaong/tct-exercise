package permutation;

import java.util.ArrayList;
import java.util.List;

public class Solution2 {
	
	public List<Integer> solution(int[] arr) {
		List<Integer> answer = new ArrayList<Integer>();
		for(int i=1; i<=arr.length; i++) {
			boolean[] visitor = new boolean[arr.length];
			permutation(arr, 0, i, new int[i], visitor, answer);
		}
		return answer;
	}

	public void permutation(int[] arr, int depth, int r, int[] value, boolean[] visitor, List<Integer> list) {
		if(depth == r) {
			//result
			StringBuilder sb = new StringBuilder();
			for(int val : value) {
				sb.append(val);
			}
			list.add(Integer.parseInt(sb.toString()));
//			System.out.println(sb.toString());
			return;
		}
		for(int i=0; i<arr.length; i++) {
			if(!visitor[i]) {
				value[depth] = arr[i];
				visitor[i] = true;
				permutation(arr, depth + 1, r, value, visitor, list);
				visitor[i] = false;
			}
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
