package programmers.level2.targetnumber;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] numbers = {1,1,1,1,1};
		int target = 3;
		System.out.println(solution(numbers, target));
	}

	public static int solution(int[] numbers, int target) {
		return checkSum(numbers, -1, 0, target);
    }
	
	public static int checkSum(int[] numbers, int idx, int sum, int target) {
		if(idx == numbers.length-1) {
			if(sum == target) {
				return 1;
			} else {
				return 0;
			}
		} else {
			idx++;
			int value = numbers[idx];
			int plus = checkSum(numbers, idx, sum+value, target);
			int minus = checkSum(numbers, idx, sum-value, target);
			return plus + minus;
		}
	}
}
