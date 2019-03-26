//Stack ��ü�� �̿��� �ڵ�

package algorithm.a05.password1;

import java.io.FileInputStream;
import java.util.Scanner;
import java.util.Stack;

class A05_Password1 {
	
	public static void main(String args[]) throws Exception	{
		System.setIn(new FileInputStream("data/password1_input.txt"));
		Scanner scanner = new Scanner(System.in);

		int caseNum = scanner.nextInt();
		for(int i=0; i<caseNum; i++) {
			String input = scanner.next();
			System.out.println("#" + (i+1) + " " + getPassword(input));
		}
		
		scanner.close();
		
	}
	
	private static String getPassword(String input) {
		Stack<Character> stack = new Stack<>();
		char[] charArray = input.toCharArray();
		for(char ch : charArray) {
			if(stack.isEmpty()) {
				stack.push(ch);
			} else {
				if(stack.peek().equals(ch)) {
					stack.pop();
				} else {
					stack.push(ch);
				}
			}
		}
		char[] array = new char[stack.size()];
		for(int i=0; i<stack.size(); i++) {
			array[i] = stack.get(i);
		}
		return String.valueOf(array);
	}
	
}