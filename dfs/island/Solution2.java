package dfs.island;

public class Solution2 {
	
	private static int[] dx = {0, 0, -1, 1};
	private static int[] dy = {1, -1, 0, 0};	
	
	public int solution(int[][] mat) {
		boolean[][] visitor = new boolean[mat.length][mat[0].length];
		int answer = 0;
		for(int i=0; i<mat.length; i++) {
			for(int j=0; j<mat[0].length; j++) {
				if(mat[i][j] == 1 && !visitor[i][j]) {
					dfs(mat, i, j, visitor);
					answer++;
				}
			}
		}
		return answer;
	}
	
	public void dfs(int[][] mat, int x, int y, boolean[][] visitor) {
		visitor[x][y] = true;
		
		for(int i=0; i<4; i++) {
			int xx = x + dx[i];
			int yy = y + dy[i];
			if(xx >= 0 && yy >= 0 && xx < mat.length && yy <mat[0].length) {
				if(!visitor[xx][yy] && mat[xx][yy] == 1) {
					dfs(mat, xx, yy, visitor);
				}
			}
		}
	}

	public static void main(String[] args) {
		int[][] mat = {
				{0, 0, 0, 0, 0},
				{0, 1, 0, 0, 0},
				{1, 1, 0, 1, 0},
				{0, 1, 1, 0, 0},
				{0, 0, 0, 1, 1},
				{0, 0, 1, 1, 1}				
		};
		Solution2 sol = new Solution2();
		System.out.println(sol.solution(mat));
	}
}
