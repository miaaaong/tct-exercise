//ť ��ü�� LinkedList�� �����Ͽ� ����

package algorithm.a06.password2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class A06_Password2 {

	public static void main(String[] args) throws FileNotFoundException {
		System.setIn(new FileInputStream("data/password2_input.txt"));
		Scanner scanner = new Scanner(System.in);

		int caseNum = scanner.nextInt();
		for(int i=0; i<caseNum; i++) {
			int[] input = new int[8];
			for(int j=0; j<8; j++) {				
				int value = scanner.nextInt();
				input[j] = value;
			}
			System.out.println("#" + (i+1) + " " + getPassword(input));
		}
		
		scanner.close();
	}
	
	private static LinkedList<Integer> getPassword(int[] input) {
		LinkedList<Integer> list = new LinkedList<>();
		for(int value : input) {
			list.add(value);
		}
		
		boolean stop = false;
		while(!stop) {
			for(int i=1; i<=5; i++) {
				Integer target = list.get(0);
				int value = target - i;
				if(value < 0) {
					value = 0;
				}
				list.removeFirst();
				list.addLast(value);
				if(value == 0) {
					stop = true;
					break;
				}
			}
		}
		return list;
	}
}
