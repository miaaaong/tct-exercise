package exercise;

public class DrawDiagonal {

	//정사각형 대각선에 숫자 1부터 시작해서 넣기 : 3 => 
	// 1 2 4
	// 3 5 7
	// 6 8 9
	public static void main(String[] args) {
		drawDiagonal(5);
	}

	public static void drawDiagonal(int length) {
		int[][] matrix = new int[length][length];
		int max = (length-1)*2;
		int number = 1;
		for(int sum=0; sum<=max; sum++) {
			if(sum < length) {
				//가장 긴 대각선까지는 첫 행부터 입력
				for(int i=0; i<=sum; i++) {
					int j=sum-i;
					matrix[i][j] = number;
					number++;
				}
			} else {
				//긴 대각선 이후는 끝 열부터 입력
				int repeat = max - sum + 1;
				for(int count=0; count<repeat; count++) {
					int j=length-1-count;
					int i=sum-j;
					matrix[i][j] = number;
					number++;
				}
			}
		}
		
		for(int i=0; i<matrix.length; i++) {
			int[] row = matrix[i];
			for(int j=0; j<row.length; j++) {
				System.out.print(row[j] + " ");
			}
			System.out.println();
		}
	}
}
