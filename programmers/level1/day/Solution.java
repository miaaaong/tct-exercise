package programmers.level1.day;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a = 5;
		int b = 24;
		Solution sol = new Solution();
		System.out.println(sol.solution(a, b));
	}

	public String solution(int a, int b) {
		int days = 0;
		int month = 1;
		while (true) {
			if (month < a) {
				days += getDays(month);
				month++;
				continue;
			} else if (month == a) {
				days += (b - 1);
				break;
			}
		}

		int remain = days % 7;
		String answer = "";
		switch (remain) {
		case 0:
			answer = "FRI";
			break;
		case 1:
			answer = "SAT";
			break;
		case 2:
			answer = "SUN";
			break;
		case 3:
			answer = "MON";
			break;
		case 4:
			answer = "TUE";
			break;
		case 5:
			answer = "WED";
			break;
		case 6:
			answer = "THU";
			break;
		}
		return answer;
	}

	private int getDays(int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		} else if (month == 2) {
			return 29;
		} else {
			return 30;
		}
	}

}
