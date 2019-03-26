package com.lgcns.tct.category;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {
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
		Map<String, List<String>> categoryMap = getCategoryMap(inputData);
		
		List<String> upList = new ArrayList<>();
		String child = categories.get(0);
		upList.add(child);
		
		boolean flag = true;
		while(flag) {
			String parent = getParent(categoryMap, child);
			if(parent != null) {				
				upList.add(parent);
				child = parent;
			} else {
				flag = false;
			}
		}
		
		String second = categories.get(1);
		if(upList.contains(second)) {
			topCategory = second;
		} else {		
			flag = true;
			while(flag) {
				String parent = getParent(categoryMap, second);
				if(upList.contains(parent)) {
					topCategory = parent;
					flag = false;
				} else {
					second = parent;
				}
			}
		}
		///////////////////////////// <-------------- 여기까지 구현 (1)
		return topCategory;
	}
	
	private String getParent(Map<String, List<String>> categoryMap, String child) {
		String parent = null;
		for(String key : categoryMap.keySet()) {
			List<String> children = categoryMap.get(key);
			if(children.contains(child)) {
				return key;
			}
		}
		return parent;
	}
	
	private Map<String, List<String>> getCategoryMap(String[][] input) {
		Map<String, List<String>> map = new HashMap<>();
		for(String[] data : input) {
			String parent = data[0];
			String child = data[1];
			if(map.containsKey(parent)) {
				map.get(parent).add(child);
			} else {
				List<String> list = new ArrayList<>();
				list.add(child);
				map.put(parent, list);
			}
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
		Map<String, List<String>> categoryMap = getCategoryMap(inputData);
		String parent = getParent(categoryMap, categoryStr);
		List<String> children = getChildren(categoryMap, parent, new ArrayList<>());
		numberOfSubcategories = children.size();
		///////////////////////////// <-------------- 여기까지 구현 (2)
		return numberOfSubcategories;
	}
	
	private List<String> getChildren(Map<String, List<String>> categoryMap, String parent, List<String> list) {
		if(categoryMap.containsKey(parent)) {			
			List<String> children = categoryMap.get(parent);
			for(String child : children) {
				list.add(child);
				getChildren(categoryMap, child, list);
			}
		}
		return list;
	}

	
}