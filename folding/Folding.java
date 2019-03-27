package com.lgcns.tct.folding;


public class Folding {
	
	
	/**
	 * 초기배열을 생성하는 기능
	 *
     * @param		inputData		int[][]		입력데이터(이차원배열)
     * @return						int[][]		생성된 초기 배열
	 */
	public int[][] getIniArr(int[][] inputData) {
		int[][] iniArr = new int[inputData.length][inputData[0].length];
		////////////////////////여기부터 구현 (1) ---------------->
		for(int i=0; i<inputData.length; i++) {
			for(int j=0; j<inputData[0].length; j++) {
				int value = inputData[i][j];
				if(value > 5) {
					iniArr[i][j] = value %5; 
				} else {
					iniArr[i][j] = value; 
				}
			}
		}
		///////////////////////////// <-------------- 여기까지 구현 (1)
		return iniArr;
	}
	
	/**
	 * 배열을 상하 좌우로 접는 기능
	 *
     * @param		iniArr		int[][]		생성된 초기 배열
     * @return					int[][]		상하 좌우로 접힌 배열	
	 */
	public int[][] getFoldingArr(int[][] iniArr) {
		int[][] foldingArr = null;
		////////////////////////여기부터 구현 (2) ---------------->
		foldingArr = changeArr(iniArr);
		
		///////////////////////////// <-------------- 여기까지 구현 (2)
		return foldingArr;
	}
	
	public int[][] changeArr(int[][] input) {
		if(input.length % 2 == 1) {
			//remove center
			int target = input.length/2;
			int[][] newArr = new int[input.length-1][input.length-1];
			for(int i=0; i<input.length-1; i++) {
				for(int j=0; j<input.length-1; j++) {
					int row = (i < target ? i : i+1);
					int col = (j < target ? j : j+1);
					newArr[i][j] = input[row][col];
				}
			}
			input = newArr;
		}
		
		//sum
		int length = input.length;
		int[][] sumArr = new int[length][length/2];
		for(int i=0; i<length/2; i++) {
			int[] a = input[i];
			int[] b = input[length-1-i];
			int[] newRow = new int[length];
			for(int j=0; j<length; j++) {
				newRow[j] = a[j] + b[j];
			}
			sumArr[i] = newRow;
		}
		
		//times
		int[][] result = new int[length/2][length/2];
		for(int i=0; i<length/2; i++) {
			for(int j=0; j<length/2; j++) {
				int a = sumArr[i][j];
				int b = sumArr[i][length-j-1];
				result[i][j] = a*b;
			}
		}
		
		return result;
	}
	
	
	/**
	 * 최종배열의 값을 구하는 기능
	 *
     * @param		foldingArr		int[][]		상하 좌우로 접힌 배열	
     * @return						int			최종배열의 값
	 */
	public int getFinalValue(int[][] foldingArr) {
		int finalValue = 0;
		////////////////////////여기부터 구현 (3) ---------------->
		while(foldingArr.length != 1) {
			foldingArr = getFoldingArr(foldingArr);
		}
		finalValue = foldingArr[0][0];
		///////////////////////////// <-------------- 여기까지 구현 (3)
		return finalValue;
	}
	
}