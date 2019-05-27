package datastructure1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class SortTest {
	
	private static List<Score> list = new ArrayList<Score>();

	public static void main(String[] args) throws IOException {
		readFile();
		
		Scanner scan = new Scanner(System.in);
		String message = "";
		while (!message.equals("QUIT")) {
			System.out.println("enter message :");
			message = scan.nextLine();
			System.out.println("entered message : " + message);
			if(message.equals("QUIT")) {
				break;
			} else if(message.equals("PRINT")) {
				printSortedList(new NameComparator());
			} else if(message.equals("KOREAN")) {
				printSortedList(new KoreanComparator());
			} else if(message.equals("ENGLISH")) {
				printSortedList(new EnglishComparator());
			} else if(message.equals("MATH")) {
				printSortedList(new MathComparator());
			}
		}
	}
	
	private static void readFile() throws IOException {
		File file = new File("./score/DS_Sample1.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while((line = br.readLine()) != null) {
			String[] split = line.split(" ");
			Score obj = new Score();
			obj.setName(split[0]);
			obj.setKorean(Integer.parseInt(split[1]));
			obj.setEnglish(Integer.parseInt(split[2]));
			obj.setMath(Integer.parseInt(split[3]));
			list.add(obj);
		}
		br.close();
		fr.close();
	}
	
	private static void printSortedList(Comparator<Score> comparator) {
		Collections.sort(list, comparator);
		for(Score score : list) {
			System.out.println(score.getName() + "\t" + score.getKorean() + "\t" + score.getEnglish() + "\t" + score.getMath());
		}
	}

}
