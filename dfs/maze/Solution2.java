package dfs.maze;

public class Solution2 {
	
	private static int[] dx = {0, 0, -1, 1};
	private static int[] dy = {-1, 1, 0, 0};	
	
	// (0,0)에서 (n,m)으로 도달하는 경로의 존재 여부
	public boolean solution(int[][] mat) {
		boolean[][] visitor = new boolean[mat.length][mat[0].length];
		if(dfs(mat, 0, 0, visitor)) {
			return true;
		}
		return false;
	}
	
	public boolean dfs(int[][] mat, int x, int y, boolean[][] visitor) {
		if(x == mat.length-1 && y == mat[0].length-1) {
			// 종료 지점
			return true;
		}
		visitor[x][y] = true;

		for(int i=0; i<4; i++) {
			int xx = x + dx[i];
			int yy = y + dy[i];
			if(xx >= 0 && yy >= 0 && xx < mat.length && yy < mat[0].length) {
				if(mat[xx][yy] == 1 && !visitor[xx][yy]) {
					if(dfs(mat, xx, yy, visitor)) {
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
				{1, 0, 0, 0, 0},
				{1, 1, 0, 0, 0},
				{1, 1, 1, 0, 0},
				{0, 1, 1, 1, 1},
				{0, 0, 1, 0, 1},
				{0, 0, 1, 0, 1}				
		};
		Solution2 sol = new Solution2();
		System.out.println(sol.solution(mat));
	}

}
