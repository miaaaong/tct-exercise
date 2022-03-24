package dfs.maze;

public class Solution {
	
	private static int[] dx = {0, 0, 1, -1};
	private static int[] dy = {1, -1, 0, 0};
	
	// 좌측 아무데서 (n,m)으로 도달하는 경로의 존재 여부
	public boolean solution(int[][] matrix) {
		boolean[][] visitor = new boolean[matrix.length][matrix[0].length];
		for(int i=0; i<matrix.length; i++) {
			if(matrix[i][0] == 1 && !visitor[i][0]) {
				if(find(matrix, i, 0, visitor)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean find(int[][] mat, int x, int y, boolean[][] visitor) {
		if(x == mat.length-1 && y == mat[0].length-1) {
			return true;
		}
		visitor[x][y] = true;
		
		for(int i=0; i<4; i++) {
			int xx = x + dx[i];
			int yy = y + dy[i];
			if(xx >= 0 && xx < mat.length && yy >= 0 && yy < mat[0].length) {
				if(mat[xx][yy] == 1 && !visitor[xx][yy]) {
					if(find(mat, xx, yy, visitor)) {
						return true;
					}
				}
			}
		}
		visitor[x][y] = false;
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] mat = {
				{0, 0, 0, 0, 0},
				{0, 1, 0, 0, 0},
				{1, 1, 1, 0, 0},
				{0, 1, 1, 1, 0},
				{1, 1, 0, 1, 1},
				{0, 1, 0, 0, 1}				
		};
		Solution sol = new Solution();
		System.out.println(sol.solution(mat));
	}

}
