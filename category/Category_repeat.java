package com.lgcns.tct.category2;


import java.util.ArrayList;
import java.util.List;

public class Category_repeat {
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
		String input = categories.get(0);
		String target = categories.get(1);
		topCategory = null;
		while(topCategory == null) {
			topCategory = getCoParent(inputData, input, target);
			if(topCategory == null) {
				input = getParent(inputData, input);
			}
		}
		///////////////////////////// <-------------- 여기까지 구현 (1)
		return topCategory;
	}
	
	private String getCoParent(String[][] inputData, String input, String target) {
		if(input.equals(target)) {
			return input;
		} else {
			String parent = getParent(inputData, target);
			if(parent == null) {
				return null;
			} else {
				return getCoParent(inputData, input, parent);
			}
		}
	}
	
	private String getParent(String[][] inputData, String child) {
		String parent = null;
		for(String[] data : inputData) {
			if(child.equals(data[1])) {
				parent = data[0];
				break;
			}
		}
		return parent;
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
		String parent = getParent(inputData, categoryStr);
		List<String> children = new ArrayList<>();
		children = getChildren(inputData, parent, children);
		numberOfSubcategories = children.size();
		///////////////////////////// <-------------- 여기까지 구현 (2)
		return numberOfSubcategories;
	}
	
	private List<String> getChildren(String[][] inputData, String category, List<String> list) {
		for(String[] data : inputData) {
			if(data[0].equals(category)) {
				list.add(data[1]);
				list = getChildren(inputData, data[1], list);
			}
		}
		return list;
	}
	
	
	
}