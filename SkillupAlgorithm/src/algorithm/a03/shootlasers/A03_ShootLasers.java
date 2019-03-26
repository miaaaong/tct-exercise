package algorithm.a03.shootlasers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class A03_ShootLasers {

	public static void main(String[] args) throws FileNotFoundException {
		int T;
		
		System.setIn(new FileInputStream("data/shootlasers_input.txt"));		
		Scanner scanner = new Scanner(System.in);
		
		T = scanner.nextInt();		
		int[] input = new int[T];
		
		for(int i=0; i<T; i++) {
			input[i] = scanner.nextInt();
		}
		
		for(int j=0; j<T; j++) {
			int value = getIndex(input, j);
			System.out.print(value + " ");
		}
		
		scanner.close();

	}

	private static int getIndex(int[] input, int idx) {
		int value = input[idx];
		int loc = 0;
		for (int i = idx - 1; i > -1; i--) {
			if (value < input[i]) {
				return (i+1);
			}
		}
		return loc;
	}

}
