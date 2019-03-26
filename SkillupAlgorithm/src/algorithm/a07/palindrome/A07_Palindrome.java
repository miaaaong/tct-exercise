//palindrome 확인할 다른 방법 : 문자열의 처음과 마지막 글자만 같은지 확인 후 같으면 문자열 양 끝 글자를 빼고 재귀호출로 반복 
//java api를 사용하지 않고 하는 방법 해보기

package algorithm.a07.palindrome;

import java.io.FileInputStream;
import java.util.Scanner;

public class A07_Palindrome {

	public static void main(String args[]) throws Exception	{
		System.setIn(new FileInputStream("data/palindrome_input.txt"));		
		Scanner scanner = new Scanner(System.in);
		
		int caseNum = scanner.nextInt();
		for(int i=0; i<caseNum; i++) {
			int size = scanner.nextInt();
			String input = scanner.next();
			int length = checkPartial(input, size);
			System.out.println("#" + (i + 1) + " " + length);
		}
		scanner.close();
	}
	
	private static int checkPartial(String input, int size) {
		if(size == 1) {
			return 0;
		} else {			
			int repeat = input.length() - size + 1;
			for(int i=0; i<repeat; i++) {
				String text = input.substring(i, i+size);
				boolean isPalindrome = isPalindrome(text);
				if(isPalindrome) {
					return size;
				}
			}
			return checkPartial(input, (size-1));
		}
	}
	
	private static boolean isPalindrome(String input) {
		boolean flag = true;
		if(input.length()%2 == 0) {
			flag = false;
		}
		
		int center = input.length()/2;
		String left = input.substring(0, center);
		
		String right = null;
		if(flag) {
			right = input.substring(center+1);
		} else {
			right = input.substring(center);
		}
		
		for(int i=0; i<left.length(); i++) {
			if(left.charAt(i) != right.charAt(left.length() - 1 - i)) {
				return false;
			}
		}
		
		return true;
		
	}
}
