package com.lgcns.test.calculator;

import java.util.Stack;

public class Calculator {
	
	private static String input1 = "5+4*3/2-1";
	private static String input2 = "3*2+3/2-2*4*2/3+5";
	private static String input3 = "3+5*4+5*6/2-3";
	private static String input4 = "2-3*4+5*4/2+9-9*2/2*2+9";

	public static void main(String[] args) {
		System.out.println(calculate(input1));
		System.out.println(calculate(input2));
		System.out.println(calculate(input3));
		System.out.println(calculate(input4));
	}
	
	private static int calculate(String input) {
		Stack<Integer> numbers = new Stack<>();
		Stack<String> operators = new Stack<>();
		
		operators.push("+");
		numbers.push(Character.getNumericValue(input.charAt(0)));
		
		int i=1;
		while(i < input.length()-1) {
			String op = Character.toString(input.charAt(i));
			int num = Character.getNumericValue(input.charAt(i+1));
			if(op.equals("+") || op.equals("-")) {
				operators.push(op);
				numbers.push(num);
			} else {
				Integer prevNum = numbers.pop();
				int value = 0;
				if(op.equals("*")) {
					value = prevNum * num;
				} else {
					value = prevNum / num;
				}
				numbers.push(value);
			}
			i += 2;
		}
		
		int result = numbers.get(0);
		for(i=1; i<operators.size(); i++) {
			String op = operators.get(i);
			int num = numbers.get(i);
			
			if(op.equals("+")) {
				result += num;
			} else {
				result -= num;
			}
		}
		
		return result;
	}

}
