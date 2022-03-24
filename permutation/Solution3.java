package permutation;

import java.util.ArrayList;
import java.util.List;

public class Solution3 {
	
	public List<Integer> solution(int[] arr) {
		List<Integer> list = new ArrayList<Integer>();
		for(int i=1; i<=arr.length; i++) {
			boolean[] visitor = new boolean[arr.length];
			int[] value = new int[i];
			permutation(arr, 0, i, value, visitor, list);
		}
		return list;
	}

	public void permutation(int[] arr, int depth, int r, int[] value, boolean[] visitor, List<Integer> list) {
		if(depth == r) {
			StringBuilder sb = new StringBuilder();
			for(int val : value) {
				sb.append(val);
			}
			list.add(Integer.parseInt(sb.toString()));
			return;
		}
		
		for(int i=0; i<arr.length; i++) {
			if(!visitor[i]) {
				value[depth] = arr[i];
				visitor[i] = true;
				permutation(arr, depth+1, r, value, visitor, list);
				visitor[i] = false;
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Solution3 sol = new Solution3();
		int[] arr = {1,2,3,4};
		List<Integer> solution = sol.solution(arr);
		for(int value : solution) {
			System.out.println(value);
		}
	}

}
