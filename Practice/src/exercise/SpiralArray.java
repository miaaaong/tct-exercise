package exercise;

public class SpiralArray {

	//N*N 배열에 나선형으로 숫자 채우기
	public static void main(String[] args) {
		int[][] sprialArray = getSprialArray(6);
		for(int[] row : sprialArray) {
			for(int val : row) {
				System.out.print(val + ",");
			}
			System.out.println();
		}
	}
	
	private static int[][] getSprialArray(int size) {
		int[][] result = new int[size][size];
		//행, 열 방향
		int dRow = 0;
		int dCol = 1;
		//진행할 단계 수
		int step = size;
		//입력할 값
		int value = 1;
		//현재 위치
		int row = 0;
		int col = 0;
		//step 반복 횟수 
		int repeat = 1;
		
		while(value <= size*size) {
			int idx = 0;
			int jdx = 0;
			for(int i=0; i<step; i++) {
				idx = row + i*dRow;
				jdx = col + i*dCol;
				result[idx][jdx] = value;
				value++;
			}
			
			//방향 전환
			if(dRow == 0 && dCol == 1) {
				//우 -> 하
				dRow = 1;
				dCol = 0;
			} else if(dRow == 1 && dCol == 0) {
				//하 -> 좌
				dRow = 0;
				dCol = -1;
			} else if(dRow == 0 && dCol == -1) {
				//좌 -> 상
				dRow = -1;
				dCol = 0;
			} else {
				//상 -> 우
				dRow = 0;
				dCol = 1;
			}
			
			//다음 작업 시작점 변경
			row = idx + dRow;
			col = jdx + dCol;
			
			//길이 설정
			if(step != size) {
				//맨 처음 size만큼 이동할 때 제외하고, 각 step별로 2번씩 반복
				if(repeat == 1) {
					repeat++;
				} else {
					//현재 step으로 2번 했으므로 step 줄이고 repeat 초기화
					repeat = 1;
					step--;
				}
			} else {
				//맨 처음 size만큼 이동했으면 step 감소
				step--;
			}
		}
		return result;
	}

}
