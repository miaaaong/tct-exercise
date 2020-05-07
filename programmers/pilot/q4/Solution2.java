package pilot.q4;

public class Solution2 {

	public static void main(String[] args) {
//		int[] numbers = {10, 40, 30, 20};
//		System.out.println(getResult(numbers, 20, 0));
		int[] numbers = {3, 7, 2, 8, 6, 4, 5, 1};
		System.out.println(getResult(numbers, 3, 0));
	}

//    public static int solution(int n) {
//        int answer = getCount(n);
//        return answer;
//    }

	private static int getResult(int[] numbers, int K, int count) {
		int[] gap = new int[numbers.length-1];
		for(int i=0; i<numbers.length-1; i++) {
			gap[i] = numbers[i] - numbers[i+1];
		}
		boolean check = true;
		int minGap = gap[0];
		int minIdx = 0;
		int maxGap = gap[0];
		int maxIdx = 0;
		for(int i=0; i<gap.length; i++) {
			if(Math.abs(gap[i]) > K) {
				check = false;
			}
			if(Math.abs(gap[i]) > Math.abs(maxGap)) {
				maxGap = gap[i];
				maxIdx = i;
			}
			if(Math.abs(gap[i]) < Math.abs(minGap)) {
				minGap = gap[i];
				minIdx = i;
			}
		}
		if(check) {
			return count;
		} else {
			//max
			int value = 0;
//			if(Math.abs(gap[maxIdx-1]) > Math.abs(gap[maxIdx+1])) {
//				value = -1;
//			} else {
//				value = 1;
//			}
			int targetIdx1 = maxIdx + value;
//			
//			//min
//			if(Math.abs(gap[minIdx-1]) > Math.abs(gap[minIdx+1])) {
//				value = -1;
//			} else {
//				value = 1;
//			}
			int targetIdx2 = minIdx + value;			
			
			//swap
			int temp = numbers[targetIdx1+1];
			numbers[targetIdx1+1] = numbers[targetIdx2+1];
			numbers[targetIdx2+1] = temp;
			return getResult(numbers, K, ++count);
		}
	}

}
