package dfs.maze;

public class Solution3 {
	
	private static int[] dx = {0, 0, -1, 1};
	private static int[] dy = {-1, 1, 0, 0};
	private static int min = 0;
	
	// (0,0)에서 (n,m)으로 도달하는 최단 경로
	public int solution(int[][] mat) {
		min = -1;
		boolean[][] visitor = new boolean[mat.length][mat[0].length];
		dfs(mat, 0, 0, visitor, 1);
		return min;
	}
	
	public void dfs(int[][] mat, int x, int y, boolean[][] visitor, int length) {
		if(x == mat.length-1 && y == mat[0].length-1) {
			// 종료 지점
			System.out.println(length);
			if(min > length) {
				min = length;
			}
			return;
		}
		visitor[x][y] = true;
		
		for(int i=0; i<4; i++) {
			int xx = x + dx[i];
			int yy = y + dy[i];
			if(xx >= 0 && yy >= 0 && xx < mat.length && yy < mat[0].length) {
				if(mat[xx][yy] == 1 && !visitor[xx][yy]) {
					dfs(mat, xx, yy, visitor, length+1);
				}
			}
		}
		visitor[x][y] = false;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] mat = {
				{1, 0, 0, 0, 0, 0},
				{1, 1, 1, 1, 1, 1},
				{1, 0, 0, 0, 0, 1},
				{1, 1, 0, 0, 0, 1},
				{0, 1, 0, 1, 1, 1},
				{0, 1, 1, 1, 0, 1}
		};
		Solution3 sol = new Solution3();
		System.out.println(sol.solution(mat));
	}

}
