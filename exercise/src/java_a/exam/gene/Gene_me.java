package com.lgcns.tct.gene;

public class Gene_me {
	/**
	 * 단순비교 방식의 유사도 측정 기능
	 * 
     * @param		inputData			String		입력데이터(유전자 문자열)
     * @return							int			유사도
	 */
	public int measureSimpleComparison(String inputData){
		int similarity = 0;
		//////////////////////여기부터 구현 (1) ---------------->
		String[] split = inputData.split(",");
		String data1 = split[0];
		String data2 = split[1];
		
		int totalLength = data1.length() + data2.length() - 1;
		String target2 = getValue(data2, totalLength, totalLength - data2.length());
		int maxStep = totalLength - data1.length();
		for(int i=0; i<=maxStep; i++) {
			String value = getValue(data1, totalLength, i);
			int sum = 0;
			for(int j=0; j<totalLength; j++) {
				if(value.charAt(j) != '-' && value.charAt(j) == target2.charAt(j)) {
					sum++;
				}
			}
			if(sum > similarity) {
				similarity = sum;
			}
		}
		
		String target1 = getValue(data1, totalLength, totalLength - data1.length());
		maxStep = totalLength - data2.length();
		for(int i=0; i<=maxStep; i++) {
			String value = getValue(data2, totalLength, i);
			int sum = 0;
			for(int j=0; j<totalLength; j++) {
				if(value.charAt(j) != '-' && value.charAt(j) == target1.charAt(j)) {
					sum++;
				}
			}
			if(sum > similarity) {
				similarity = sum;
			}
		}
		
		///////////////////////////// <-------------- 여기까지 구현 (1)
		return similarity;
	}
	
	private String getValue(String data, int length, int step) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<step; i++) {
			sb.append("-");
		}
		sb.append(data);
		int remain = length - data.length() - step;
		for(int i=0; i<remain; i++) {
			sb.append("-");
		}
		return sb.toString();
	}
	
	/**
	 * 행렬비교 방식의 유사도 측정 기능
	 * 
     * @param		inputData				String		입력데이터(유전자 문자열)
     * @param		similarityMatrix		int[][]		입력데이터(유사도 측정 행렬)
     * @return								int			가장 큰 유사도
	 */
	public int measureSortComparison(String inputData, int[][] similarityMatrix){
		int maxSimilarity = 0;
		//////////////////////여기부터 구현 (2) ---------------->
		String[] split = inputData.split(",");
		String data1 = split[0];
		String data2 = split[1];
		
		int totalLength = data1.length() + data2.length() - 1;
		String target2 = getValue(data2, totalLength, totalLength - data2.length());
		int maxStep = totalLength - data1.length();
		boolean isFirst = true;
		for(int i=0; i<=maxStep; i++) {
			String value = getValue(data1, totalLength, i);
			int sum = 0;
			for(int j=0; j<totalLength; j++) {
				int row = getIndex(value.charAt(j));
				int col = getIndex(target2.charAt(j));
				sum += similarityMatrix[row][col];
			}
			if(isFirst) {
				maxSimilarity = sum;
				isFirst = false;
			} else if(sum > maxSimilarity) {
				maxSimilarity = sum;
			}
		}
		
		String target1 = getValue(data1, totalLength, totalLength - data1.length());
		maxStep = totalLength - data2.length();
		for(int i=0; i<=maxStep; i++) {
			String value = getValue(data2, totalLength, i);
			int sum = 0;
			for(int j=0; j<totalLength; j++) {
				int row = getIndex(value.charAt(j));
				int col = getIndex(target1.charAt(j));
				sum += similarityMatrix[row][col];
			}
			if(sum > maxSimilarity) {
				maxSimilarity = sum;
			}
		}
		
		///////////////////////////// <-------------- 여기까지 구현 (2)
		return maxSimilarity;
	}
	
	private int getIndex(char value) {
		int idx = 0;
		switch (value) {
		case 'A' : 
			idx = 0;
			break;
		case 'C' : 
			idx = 1;
			break;
		case 'G' : 
			idx = 2;
			break;
		case 'T' : 
			idx = 3;
			break;
		default : 
			idx = 4;
		}
		return idx;
	}
}
