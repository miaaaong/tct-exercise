package exercise;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BraceOperation_Repeat {

	/**
	 * 아래와 같은 규칙을 가진 괄호식 계산하기
	 * ():2
	 * []:3
	 * ()[]:2+3
	 * ([]):2*3
	 */
	public static void main(String[] args) {
		String input = "(()([(()[])])([][]))";
		System.out.println(processBrace(input));
	}
	
	public static int processBrace(String input) {
		int result = 0;
		input = changeNum(input);
		while(true) {
			System.out.println(input);
			input = processMultiply(input);
			System.out.println(input);
			input = processSum(input);
			try {
				result = Integer.parseInt(input);
				break;
			} catch (NumberFormatException e) {
				continue;
			}
		}
		return result;
	}
	
	public static String changeNum(String input) {
		return input.replaceAll("\\(\\)", "+2").replaceAll("\\[\\]", "+3");
	}
	
	//"+숫자"로 연달아 있는 부분 연산
	public static String processSum(String input) {
		String exp = "(\\+[0-9]+){2,}";
		Pattern pattern = Pattern.compile(exp);
		Matcher matcher = pattern.matcher(input);
		while(matcher.find()) {
			int sum = 0;
			String group = matcher.group();
			//sum 연산
			String[] split = group.split("\\+");
			for(int i = 1; i<split.length; i++) {
				sum += Integer.parseInt(split[i]);
			}
			input = matcher.replaceFirst("+" + sum);
			matcher = pattern.matcher(input);
		}
		return input;
	}
	
	//"("이나 "["로 시작하여 "+숫자"가 있고 "]"이나 ")"로 끝나는 부분 연산
	public static String processMultiply(String input) {
		String exp = "([\\[,\\(]\\+[0-9]+[\\],\\)])";
		Pattern pattern = Pattern.compile(exp);
		Matcher matcher = pattern.matcher(input);
		while(matcher.find()) {
			String group = matcher.group();
			//숫자 꺼내기
			Matcher numMatcher = Pattern.compile("[0-9]+").matcher(group);
			numMatcher.find();
			String target = numMatcher.group();
			int number = Integer.parseInt(target);
			int result = 0;
			if(group.startsWith("(")) {
				result = number * 2;
			} else {
				result = number * 3;
			}
			
			input = matcher.replaceFirst("+" + result);
			matcher = pattern.matcher(input);
		}
		return input;
	}
	

}
