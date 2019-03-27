package exercise;

public class SumOfSquareNumber {

	//자연수의 제곱의 합이 자기 자신이 되는 숫자 찾기 : 35 = 1^2 + 3^2 + 5^2
	public static void main(String[] args) {
		for(int i=1; i<=100; i++) {
			System.out.println(i + " : " + isTrue(i));
		}
	}

	public static boolean isTrue(int num) {
		int max = (int) Math.sqrt(num);
		for(int i=max; i>0; i--) {
			boolean result = findCombination(i, num);
			if(result) {
				return result;
			}
		}
		return false;
	}
	
	public static boolean findCombination(int startNum, int target) {
		int sum = 0;
		for(int i=startNum; i>0; i--) {
			int value = i*i;
			sum += value;
			if(sum == target) {
				return true;
			} else if(sum > target) {
				sum -= value;
				//연산 횟수를 줄인다면...
				int temp = (int) Math.sqrt(target - sum);
				i = temp+1;
			}
		}
		return false;
	}
}
