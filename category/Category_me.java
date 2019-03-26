package com.lgcns.tct.category;


import java.util.ArrayList;
import java.util.List;

public class Category_me {
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
		String[] index = getInfo(inputData, categories.get(0));
		String[] target = getInfo(inputData, categories.get(1));
		topCategory = getParent(inputData, categories.get(0), index, target);
		///////////////////////////// <-------------- 여기까지 구현 (1)
		return topCategory;
	}
	
	private String getParent(String[][] input, String value, String[] index, String[] target) {
		if(index[0].equals(target[0])) {
			return index[0];
		} else {
			//get parent info
			String[] parentInfo = getInfo(input, index[0]);
			if(parentInfo == null) {
				//최상위
				//get target's parent info
				String[] newTarget = getInfo(input, target[0]);
				String[] valueInfo = getInfo(input, value);
				return getParent(input, value, valueInfo, newTarget);
			} else {
				return getParent(input, value, parentInfo, target);
			}
		}
	}
	
	private String[] getInfo(String[][] input, String value) {
		for(String[] info : input) {
			if(info[1].equals(value)) {
				return info;
			}
		}
		return null;
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
		String[] info = getInfo(inputData, categoryStr);
		numberOfSubcategories = getSum(inputData, info[0], 0) - 1;
		///////////////////////////// <-------------- 여기까지 구현 (2)
		return numberOfSubcategories;
	}
	
	private int getSum(String[][] input, String value, int sum) {
		List<String[]> children = getChildren(input, value);
		for(String[] child : children) {
			sum = getSum(input, child[1], sum);
		}
		sum++;
		return sum;
	}
	
	private List<String[]> getChildren(String[][] input, String value) {
		List<String[]> children = new ArrayList<>();
		for(String[] info : input) {
			if(info[0].equals(value)) {
				children.add(info);
			}
		}
		return children;
	}

}