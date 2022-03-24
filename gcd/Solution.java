package gcd;

public class Solution {

	public int solution(int num1, int num2) {
		int a = Math.max(num1, num2);
		int b = Math.min(num1, num2);
		
		while(a%b > 0) {
			int mod = a%b;
			a = b;
			b = mod;
		}
		
		return b;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num1 = 12;
		int num2 = 8;
		Solution sol = new Solution();
		System.out.println(sol.solution(num1, num2));
	}

}
