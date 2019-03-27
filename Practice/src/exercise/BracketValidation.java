package exercise;

import java.util.Stack;

public class BracketValidation {

	public static void main(String[] args) {
		String input = "(aa)(bb({dd)ee)(ff))";
		System.out.println(validate(input));
		input = "(aa)(bb(dd)ee)(ff))";
		System.out.println(validate(input));
		input = "((aa)(bb(dd)ee)(ff))";
		System.out.println(validate(input));
		input = "{((aa)(bb(dd)ee)(ff))";
		System.out.println(validate(input));
		input = "(aa))(bb(dd)ee)(ff)";
		System.out.println(validate(input));
	}

	public static boolean validate(String input) {
		Stack<String> stack = new Stack<>();
		char[] charArray = input.toCharArray();
		for(char point : charArray) {
			String value = String.valueOf(point);
			if(value.equals("(") || value.equals("{")) {
				stack.push(value);
			} else if(value.equals(")") || value.equals("}")) {
				if(stack.isEmpty()) {
					//여는 괄호가 stack에 있어야 하는데 stack에 아무것도 없으면 짝이 안 맞으니까 틀린 괄호
					return false;
				}
				String peek = stack.peek();
				if(peek.equals("(") && value.equals(")")) {
					//짝이 맞으니까 stack에서 제거
					stack.pop();
				} else if(peek.equals("{") && value.equals("}")) {
					//짝이 맞으니까 stack에서 제거
					stack.pop();
				} else {
					//짝이 안 맞으니까 틀린 괄호
					return false;
				}
			}
		}
		if(stack.isEmpty()) {
			return true;
		} else {
			//아직 여는 괄호가 남아있을 수도....
			return false;
		}
	}
}
