package pilot.q2;

public class Solution {

	public static void main(String[] args) {
		int[] result = solution(6, 25);
		for(int i=0; i<12; i++) {
			System.out.println(result[i]);
		}
	}

	public static int[] solution(int day, int k) {
		int[] answer = getDays(day, k);
		return answer;
	}

	private static int[] getDays(int day, int target) {
		int[] days = new int[12];
		int[] numbers = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		days[0] = getFirstDay(day, target);
		for (int i = 1; i < 12; i++) {
			int value = numbers[i - 1] % 7;
			int targetDay = days[i - 1] + value;
			if (targetDay > 6) {
				targetDay -= 7;
			}
			days[i] = targetDay;
		}

		int[] result = new int[12];
		for (int i = 0; i < 12; i++) {
			result[i] = (days[i] < 5 ? 0 : 1);
		}
		return result;
	}

	private static int getFirstDay(int day, int target) {
		if (target == 1) {
			return day;
		} else {
			int value = (target - 1) % 7;
			int targetDay = day + value;
			if (targetDay > 6) {
				targetDay -= 7;
			}
			return targetDay;
		}
	}

}
