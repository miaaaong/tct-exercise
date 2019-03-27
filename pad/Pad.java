package com.lgcns.tct.pad;

public class Pad {

	/**
	 * 이동문자열을 추출하는 기능
	 *
	 * @param 	inputData   	String			입력데이터(문자열)
	 * @return 					String 			이동문자열
	 */
	public String getRotationStr(String inputData) {
		String rotationStr = "";
		//////////////////////// 여기부터 구현 (1) ---------------->
		for(int i=0; i<inputData.length()/2; i++) { 
			char type = inputData.charAt(i*2+1);
			if(type == 'U' || type == 'D' || type == 'L' || type == 'R') {
				if(inputData.charAt(i*2) != '0') {					
					rotationStr += inputData.substring(i*2, i*2+2);
				}
			}
		}
		
		///////////////////////////// <-------------- 여기까지 구현 (1)
		return rotationStr;
	}

	/**
	 * 숫자패드를 이동시키는 기능
	 *
	 * @param 	inputNumberPad   	int[][]			입력데이터(숫자패드)
	 * @param 	rotationStr   		String			이동문자열
	 * @return 						int[][]			이동된 숫자패드
	 */
	public int[][] getNumberPad(int[][] inputNumberPad, String rotationStr) {
		int[][] numberPad = null;
		//////////////////////// 여기부터 구현 (2) ---------------->
		numberPad = inputNumberPad;
		for(int i=0; i<rotationStr.length()/2; i++) {
			int count = Integer.parseInt(String.valueOf(rotationStr.charAt(i*2)));
			char type = rotationStr.charAt(i*2+1);
			if(type == 'U') {
				numberPad = getRowChangedPad(numberPad, -count);
			} else if(type == 'D') {
				numberPad = getRowChangedPad(numberPad, count);
			} else if(type == 'L') {
				numberPad = getColChangedPad(numberPad, -count);
			} else if(type == 'R') {
				numberPad = getColChangedPad(numberPad, count);
			}
		}
		
		///////////////////////////// <-------------- 여기까지 구현 (2)
		return numberPad;
	}
	
	public int[][] getRowChangedPad(int[][] pad, int count) {
		int[][] result = new int[pad.length][pad.length];
		for(int i=0; i<pad.length; i++) {
			int[] data = pad[i];
			
			int idx = i + count;
			if(idx >= pad.length) {
				idx -= pad.length;
			} else if(idx < 0) {
				idx += pad.length;
			}
			
			result[idx] = data;
		}
		return result;
	}
	
	public int[][] getColChangedPad(int[][] pad, int count) {
		int[][] result = new int[pad.length][pad.length];
		for(int i=0; i<pad.length; i++) {
			int[] data = new int[pad.length];
			for(int j=0; j<pad.length; j++) {
				data[j] = pad[j][i];
			}
			
			int idx = getIndex(i, count, pad.length);
			
			for(int j=0; j<pad.length; j++) {
				result[j][idx] = data[j];
			}
		}
		return result;
	}
	
	public int getIndex(int point, int count, int length) {
		int result = point + count;
		while(result < 0 || result >= length) {
			if(result < 0) {
				result += length;
			} else {
				result -= length;
			}
		}
		return result;
	}

}