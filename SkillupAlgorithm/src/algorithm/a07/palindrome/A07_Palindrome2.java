package algorithm.a07.palindrome;

import java.io.FileInputStream;
import java.util.Scanner;

public class A07_Palindrome2 {

	public static void main(String args[]) throws Exception	{
		System.setIn(new FileInputStream("data/palindrome_input.txt"));
		Scanner scanner = new Scanner(System.in);

		int caseNum = scanner.nextInt();
		for(int i=0; i<caseNum; i++) {
			int size = scanner.nextInt();
			String input = scanner.next();
			System.out.println("#" + (i+1) + " " + getPalindromLength(input, size));
		}
		
		scanner.close();
	
	}
	
	private static int getPalindromLength(String input, int size) {
		int repeat = input.length() - size + 1;
		for(int i=0; i<repeat; i++) {
			String target = input.substring(i, i+size);
			boolean palindrome = isPalindrome(target);
			if(palindrome) {
				return size;
			}
		}
		return getPalindromLength(input, size-1);
	}
	
	private static boolean isPalindrome(String input) {
		if(input.length() == 1) {
			return true;
		} else {
			if(input.charAt(0) == input.charAt(input.length()-1)) {
				if(input.length() == 2) {
					return true; 
				} else {
					return isPalindrome(input.substring(1, input.length()-1));
				}
			} else {
				return false;
			}
		}
	}
}
