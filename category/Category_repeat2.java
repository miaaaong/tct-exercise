package com.lgcns.tct.category.repeat;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category_repeat2 {
	/**
	 * 상위 카테고리를 검색하는 기능
	 *
     * @param		inputData		String[][]		입력데이터(카테고리 정보)
     * @param		categories		List			입력데이터(inputCategories[0]: 카테고리1, inputCategories[1]: 카테고리2)
     * @return						String 			상위 카테고리
	 */
	public String getTopCategory(String[][] inputData, List<String> categories) {
		String topCategory = "";
		////////////////////////여기부터 구현 (1) ---------------->
		List<String> list1 = getList(inputData, categories.get(0));
		List<String> list2 = getList(inputData, categories.get(1));
		
		for(String val1 : list1) {
			for(String val2 : list2) {
				if(val1.equals(val2)) {
					topCategory = val1;
					break;
				}
			}
			if(!topCategory.equals("")) {
				break;
			}
		}
		
		///////////////////////////// <-------------- 여기까지 구현 (1)
		return topCategory;
	}
	
	private List<String> getList(String[][] inputData, String target) {
		List<String> list = new ArrayList<>();
		list.add(target);
		while(target != null) {
			boolean toContinue = false;
			for(String[] data : inputData) {
				if(target.equals(data[1])) {
					list.add(data[0]);
					target = data[0];
					toContinue = true;
					break;
				}
			}
			if(!toContinue) {				
				target = null;
			}
		}
		return list;
	}
	
	private Map<String, List<String>> getCategoryMap(String[][] inputData) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for(String[] input : inputData) {
			String parent = input[0];
			String child = input[1];
			List<String> children = null;
			if(map.containsKey(parent)) {
				children = map.get(parent);
			} else {
				children = new ArrayList<String>();
			}
			children.add(child);
			map.put(parent, children);
		}
		
		return map;
	}
	
	/**
	 * 하위 카테고리의 개수를 계산하는 기능
	 *
     * @param		inputData		String[][]		입력데이터(카테고리 정보)
     * @param		categoryStr		String			입력데이터(카테고리)
     * @return						int 			하위 카테고리의 개수
	 */
	public int getNumberOfSubcategories(String[][] inputData, String categoryStr) {
		int numberOfSubcategories = 0;
		////////////////////////여기부터 구현 (2) ---------------->
		List<String> list = getList(inputData, categoryStr);
		String parent = list.get(1);
		Map<String, List<String>> map = getCategoryMap(inputData);
		numberOfSubcategories = getChildrenNumber(map, parent, 0);
		///////////////////////////// <-------------- 여기까지 구현 (2)
		return numberOfSubcategories;
	}
	
	private int getChildrenNumber(Map<String, List<String>> map, String current, int count) {
		List<String> list = map.get(current);
		if(list == null) {
			return count;
		}
		count += list.size();
		for(String child : list) {
			count = getChildrenNumber(map, child, count);
		}
		return count;
	}
	
	
}