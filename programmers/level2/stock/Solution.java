package programmers.level2.stock;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] prices = {1,2,3,2,3};
		int[] result = solution(prices);
		for(int val : result) {
			System.out.println(val);
		}
	}
	
	public static int[] solution(int[] prices) {
		int[] answer = new int[prices.length];
		for(int i=0; i<prices.length-1; i++) {
			int stock = prices[i];
			int value = -1;
			for(int j=i+1; j<prices.length; j++) {
				int target = prices[j];
				if(stock > target) {
					value = j-i;
					break;
				}
			}
			if(value == -1) {
				value = prices.length - i - 1;
			}
			answer[i] = value;
		}
		return answer;
	}

}
