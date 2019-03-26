package algorithm.a01.diversity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class A01_Diversity {

	public static void main(String[] args) throws FileNotFoundException {

		int T;

		System.setIn(new FileInputStream("data/diversity_input.txt"));
		Scanner scanner = new Scanner(System.in);

		T = scanner.nextInt();

		for (int i = 0; i < T; i++) {
			String nextLine = scanner.next();
			System.out.println("#" + (i + 1) + " " + getDiversity(nextLine));
		}

		scanner.close();

	}

	private static int getDiversity(String data) {
		// 0~9까지 각 개수 정보
		int[] count = new int[10];

		char[] charArray = data.toCharArray();
		for (char ch : charArray) {
			int num = Integer.valueOf(String.valueOf(ch));
			count[num]++;
		}

		int diversity = 0;
		for (int value : count) {
			if (value > 0) {
				diversity++;
			}
		}
		return diversity;
	}

}
