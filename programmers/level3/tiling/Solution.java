package programmers.level3.tiling;

public class Solution {

	public int solution(int n) {
		if(n < 3) {
			return n;
		} else {
			int a = 1;
			int b = 2;
			int c = 0;
			for(int i=3; i<=n; i++) {
				c = (a+b)% 1000000007;
				a = b;
				b = c;
			}
			return c;
		}
		
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Solution solution = new Solution();
		System.out.println(solution.solution(4));
	}
	
}
