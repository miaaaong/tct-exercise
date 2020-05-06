package programmers.demo_a.palindrome;

class Solution {
	public int solution(int n, int m) {
		int answer = 0;
		
		for(int i=n; i<=m; i++) {
			char[] input = Integer.toString(i).toCharArray();
			int length = input.length;
			boolean result = true;
			for(int j=0; j<length/2; j++) {
				if(input[j] != input[length-1-j]) {
					result = false;
					break;
				}
			}
			if(result) {
				answer++;
			}
		}

		return answer;
	}
	
}
