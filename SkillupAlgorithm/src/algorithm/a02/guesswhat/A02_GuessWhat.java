package algorithm.a02.guesswhat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.io.FileInputStream;

public class A02_GuessWhat {
	
	public static void main(String args[]) throws Exception {
		int T;
		
		System.setIn(new FileInputStream("data/guesswhat_input.txt"));		
		Scanner scanner = new Scanner(System.in);
		
		T = scanner.nextInt();
		
		for(int i=0; i<T; i++) {
			int students = scanner.nextInt();
			int[] records = new int[students];
			for(int j=0; j<students; j++) {
				records[j] = scanner.nextInt();
			}
			int value = getValue(records);
			
			System.out.println("#" + (i+1) + " " + value);
		}
		
		scanner.close();
		
	}
	
	private static int getValue(int[] records) {
		Map<Integer, Integer> map = new HashMap<>();
		for(int value : records) {
			if(map.containsKey(value)) {
				map.put(value, map.get(value) + 1);
			} else {
				map.put(value, 1);
			}
		}
		int target = 0;
		int count = 0;
		Iterator<Integer> iterator = map.keySet().iterator();
		while(iterator.hasNext()) {
			Integer record = iterator.next();
			int total = map.get(record);
			if(total > count) {
				target = record;
				count = total;
			} else if(total == count) {
				if(record.compareTo(target) > 0) {
					target = record;
				}
			}
		}
		return target;
	}
}