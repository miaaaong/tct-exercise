package programmers.level3.network;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		int n = 3;
//		int[][] computers = {{1, 1, 0}, {1, 1, 0}, {0, 0, 1}};
		int n = 3;
		int[][] computers = { { 1, 1, 0 }, { 1, 1, 1 }, { 0, 1, 1 } };
		System.out.println(solution(n, computers));
	}

	public static int solution(int n, int[][] computers) {
		int answer = 0;
		Set<Integer> set = new HashSet<Integer>();
		for(int i=0; i<computers.length; i++) {
			if(!set.contains(i)) {
				dfs(computers, i, set);
				answer++;
			}
		}
		return answer;
	}

	public static void dfs(int[][] a, int v, Set<Integer> set) {
		int n = a[0].length;
		Stack<Integer> stack = new Stack<>();
		boolean[] c = new boolean[n];
		stack.push(v);
		set.add(v);
		c[v] = true;
		boolean flag;
		while (!stack.isEmpty()) {
			int vv = stack.peek();
			flag = false;
			for (int i = 0; i < n; i++) {
				if (a[vv][i] == 1 && !c[i]) {
					stack.push(i);
					set.add(i);
					c[i] = true;
					flag = true;
					break;
				}
			}
			if (!flag) {
				stack.pop();
			}
		}
	}

}
