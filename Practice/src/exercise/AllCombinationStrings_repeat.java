package exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllCombinationStrings_repeat {

	//주어진 문자열의 문자들을 모두 재배열하여 만들 수 있는 모든 문자열들을 정렬하고 주어진 문자열 이후부터 출력하기 : BCDA -> BDAC, BDCA, CABD, CADB .... 
	public static void main(String[] args) {
		String input = "BCDA";
		
		List<List<String>> list = new ArrayList<>();
		List<String> init = new ArrayList<>();
		init.add("");
		init.add(input);
		list.add(init);
		list = getValueList(list);
		
		List<String> sortList = getSortList(list, input);
		
		for(String value : sortList) {
			System.out.println(value);
		}
	}
	
	private static List<String> getSortList(List<List<String>> list, String input) {
		//get string values
		List<String> values = new ArrayList<>();
		for(List<String> data : list) {
			String val = data.get(0);
			//중복 제거
			if(values.indexOf(val) < 0) {
				values.add(val);
			}
		}
		//오름차순 정렬
		Collections.sort(values);
		
		//주어진 문자열 인덱스 찾기
		int idx = values.indexOf(input);
		
		return values.subList(idx+1, values.size());
	}
	
	private static List<List<String>> getValueList(List<List<String>> list) {
		List<List<String>> newList = new ArrayList<>();
		for(List<String> data : list) {
			//현재 조합 글자
			String value = data.get(0);
			//남은 글자
			String target = data.get(1);
			if(target.length() == 0) {
				//남은 글자가 없으면 종료
				return list;
			}
			char[] charArray = target.toCharArray();
			//남은 글자에서 차례로 하나씩 골라 현재 조합 글자에 추가하고, 남은 글자 리스트에서 삭제 후 반영
			for(int i=0; i<charArray.length; i++) {
				//조합 글자 갱신
				String newValue = value + String.valueOf(charArray[i]);
				//조합에 쓴 글자를 제외하고 남은 글자 리스트 갱신
				String newTarget = "";
				for(int j=0; j<charArray.length; j++) {
					if(j != i) {
						newTarget += String.valueOf(charArray[j]);
					}
				}
				//갱신한 값들로 다시 리스트 작성
				List<String> newData = new ArrayList<>();
				newData.add(newValue);
				newData.add(newTarget);
				
				newList.add(newData);
			}
		}
		return getValueList(newList);
	}

}
