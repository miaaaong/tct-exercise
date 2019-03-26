package algorithm.a04.tilesetting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class A04_Tilesetting {

	public static void main(String[] args) throws FileNotFoundException {
		System.setIn(new FileInputStream("data/tilesetting_input.txt"));
		Scanner scanner = new Scanner(System.in);
		
		int caseNum = scanner.nextInt();
		
		for(int i=0; i<caseNum; i++) {
			int width = scanner.nextInt();
			int height = scanner.nextInt();
			int[] result = getTiles(width, height);
			int length = result[0];
			int count = result[1];
			System.out.println("#" + (i+1) + " " + length + "m - " + count + "개");
		}

		scanner.close();
	}
	
	private static int[] getTiles(int width, int height) {
		int max = getMax(width, height);
		int count = (width/max) * (height/max);
		return new int[]{max, count};
	}
	
	private static int getMax(int width, int height) {
		int a = 0;
		int b = 0;
		if(width > height) {
			a = width;
			b = height;
		} else {
			a = height;
			b = width;
		}
		
		while(b != 0) {
			int mod = a%b; 
			a = b;
			b = mod;
		}
		return a;
	}
}