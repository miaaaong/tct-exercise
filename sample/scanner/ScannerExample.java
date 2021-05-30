package sample.scanner;

import java.util.Scanner;

public class ScannerExample {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		String message = "";
		while (!message.equals("q")) {
			System.out.println("enter message :");
			message = scan.nextLine();
			System.out.println("entered message : " + message);
		}

		System.out.println("q is entered.. finish.");

		// 띄어쓰기로 구분하여 한번에 여러 개 읽기
		System.out.println("-------------------");
		System.out.println("enter 2 integers : ");
		int a = scan.nextInt();
		int b = scan.nextInt();

		System.out.println("entered integers : " + a + ", " + b);
		System.out.println("-------------------");
		System.out.println("enter 2 doubles : ");
		double c = scan.nextDouble();
		double d = scan.nextDouble();

		System.out.println("entered doubles : " + c + ", " + d);
	}
}
