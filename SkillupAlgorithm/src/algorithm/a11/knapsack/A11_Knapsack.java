package algorithm.a11.knapsack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class A11_Knapsack {

	public static void main(String[] args) throws FileNotFoundException {
		System.setIn(new FileInputStream("data/knapsack_input.txt"));		
		Scanner scanner = new Scanner(System.in);
		
		int caseNum = scanner.nextInt();
		for(int num=0; num<caseNum; num++) {			
			int types = scanner.nextInt();
			int capacity = scanner.nextInt();
			int[][] input = new int[types][2];
			for(int i=0; i<types; i++) {
				int weight = scanner.nextInt();
				int price = scanner.nextInt();
				input[i] = new int[]{weight, price};
			}
			System.out.println("#" + (num + 1) + " " + getMaxValue(input, capacity));
		}
		
		scanner.close();

	}
	
	private static int getMaxValue(int[][] input, int capacity) {
		List<List<int[]>> allPossibleList = getAllPossibleList(input, capacity);
		int maxValue = 0;
		for(List<int[]> list : allPossibleList) {
			int listValue = getListValue(list);
			if(listValue > maxValue) {
				maxValue = listValue;
			}
		}
		return maxValue;
	}
	
	private static int getListValue(List<int[]> list) {
		int value = 0;
		for(int[] info : list) {
			value += info[1];
		}
		return value;
	}
	
	//순서대로 각 물품을 넣는 경우, 안 넣는 경우 모두를 list로 생성
	private static List<List<int[]>> getAllPossibleList(int[][] input, int capacity) {
		List<List<int[]>> totalList = new ArrayList<>();
		for(int i=0; i<input.length; i++) {
			int[] info = input[i];
			//copy - 이전 모든 list를 복사해둠 (현재 물품을 안 넣는 경우의 데이터 유지를 위해)
			List<List<int[]>> copiedList = copy(totalList);
			
			//add to existing list - 모든 list에 현재 물품을 추가, 단 capacity를 넘지 않는 경우에만
			if(totalList.size() == 0) {
				//첫번째 물품은 빈 array와 포함된 array (capacity보다 작은 경우)를 명시적으로 만듦 
				List<int[]> emptyList = new ArrayList<>();
				totalList.add(emptyList);
				if(info[0] <= capacity) {					
					List<int[]> addedList = new ArrayList<>();
					addedList.add(info);
					totalList.add(addedList);
				}
			} else {				
				for(List<int[]> list : totalList) {
					if(checkCapacity(list, info[0], capacity)) {					
						list.add(info);
					}
				}
			}
			
			//add all copy to total list - 현재 물품을 넣지 않는 경우의 list도 결과에 추가
			totalList.addAll(copiedList);
		}
		return totalList;
	}
	
	private static boolean checkCapacity(List<int[]> list, int currWeight, int totalCapacity) {
		int sum = 0;
		for(int[] info : list) {
			sum += info[0];
		}
		if(totalCapacity - sum >= currWeight) {
			return true;
		} else {
			return false;
		}
	}
	
	private static List<List<int[]>> copy(List<List<int[]>> totalList) {
		List<List<int[]>> newList = new ArrayList<>();
		for(List<int[]> list : totalList) {
			List<int[]> copyList = new ArrayList<>();
			for(int[] info : list) {
				copyList.add(new int[]{info[0], info[1]});
			}
			newList.add(copyList);
		}
		return newList;
	}
	
}
