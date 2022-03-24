package dfs.island;

public class Solution {
	
	private static int[] dx = {0, 0, 1, -1};
	private static int[] dy = {1, -1, 0, 0};
	
	public int solution(int[][] matrix) {
		int answer = 0;
		boolean[][] visitor = new boolean[matrix.length][matrix[0].length];
		for(int i=0; i<matrix.length; i++) {
			for(int j=0; j<matrix[0].length; j++) {
				if(matrix[i][j] == 1 && !visitor[i][j]) {
					answer += find(matrix, i, j, visitor);
				}
			}
		}
		return answer;
	}
	
	public int find(int[][] mat, int x, int y, boolean[][] visitor) {
		visitor[x][y] = true;
		
		for(int i=0; i<4; i++) {
			// ©Л аб го ╩С
			int xx = x + dx[i];
			int yy = y + dy[i];
			if(xx >= 0 && xx < mat.length && yy >= 0 && yy < mat[0].length) {
				if(mat[xx][yy] == 1 && !visitor[xx][yy]) {
					find(mat, xx, yy, visitor);
				}
			}
		}
		return 1;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] mat = {
				{0, 0, 0, 0, 0},
				{0, 1, 0, 0, 0},
				{1, 1, 1, 0, 0},
				{0, 1, 1, 0, 0},
				{0, 0, 0, 1, 1},
				{0, 0, 0, 1, 1}				
		};
		Solution sol = new Solution();
		System.out.println(sol.solution(mat));
	}

}
