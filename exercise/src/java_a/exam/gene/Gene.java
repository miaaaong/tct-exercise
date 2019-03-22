package java_a.exam.gene;

public class Gene {
	/**
	 * 단순비교 방식의 유사도 측정 기능
	 * 
     * @param		inputData			String		입력데이터(유전자 문자열)
     * @return							int			유사도
	 */
	public int measureSimpleComparison(String inputData){
		int similarity = 0;
		//////////////////////여기부터 구현 (1) ---------------->
		String[] input = inputData.split(",");
		int size = input[0].length() + input[1].length();

		String target1 = "";
		String target2 = getTemp(input[1], size, input[0].length());
		for(int i=1; i<=input[1].length(); i++) {
			int count = 0;
			target1 = getTemp(input[0], size, i);
			for(int j=0; j<size; j++) {
				if(target1.charAt(j) != '-' && target1.charAt(j) == target2.charAt(j)) {
					count++;
				}
			}
			if(count > similarity) {
				similarity = count;
			}
		}
		
		for(int i=1; i<=input[0].length(); i++) {
			int count = 0;
			target2 = getTemp(input[1], size, input[0].length()-i);
			for(int j=0; j<size; j++) {
				if(target1.charAt(j) != '-' && target1.charAt(j) == target2.charAt(j)) {
					count++;
				}
			}
			if(count > similarity) {
				similarity = count;
			}
		}
		
		///////////////////////////// <-------------- 여기까지 구현 (1)
		return similarity;
	}
	
	private String getTemp(String input, int size, int step) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<step; i++) {
			sb.append("-");
		}
		sb.append(input);
		for(int i=0; i<(size - input.length() - step); i++) {
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
		String[] input = inputData.split(",");
		int size = input[0].length() + input[1].length();

		String target1 = "";
		String target2 = getTemp(input[1], size, input[0].length());
		for(int i=1; i<=input[1].length(); i++) {
			int sum = 0;
			target1 = getTemp(input[0], size, i);
			for(int j=0; j<size; j++) {
				sum += similarityMatrix[getMatrixIdx(target1.charAt(j))][getMatrixIdx(target2.charAt(j))];
			}
			if(sum > maxSimilarity) {
				maxSimilarity = sum;
			}
		}
		
		for(int i=1; i<=input[0].length(); i++) {
			int sum = 0;
			target2 = getTemp(input[1], size, input[0].length()-i);
			for(int j=0; j<size; j++) {
				sum += similarityMatrix[getMatrixIdx(target1.charAt(j))][getMatrixIdx(target2.charAt(j))];
			}
			if(sum > maxSimilarity) {
				maxSimilarity = sum;
			}
		}
		///////////////////////////// <-------------- 여기까지 구현 (2)
		return maxSimilarity;
	}
	
	private int getMatrixIdx(char val) {
		int idx = 0;
		if(val == 'A') {
			idx = 0;
		} else if(val == 'C') {
			idx = 1;
		} else if(val == 'G') {
			idx = 2;
		} else if(val == 'T') {
			idx = 3;
		} else {
			idx = 4;
		}
		return idx;
	}
		
}
