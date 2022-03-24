package bfs.island;

import java.util.LinkedList;
import java.util.Queue;

public class Solution2 {
	
	private static int[] dx = {0,0,1,-1};
	private static int[] dy = {1,-1,0,0};

	// 1로 구성된 섬 개수 
	public int solution(int[][] mat) {
		int answer = 0;
		boolean[][] visitor = new boolean[mat.length][mat[0].length];
		for(int i=0; i<mat.length; i++) {
			for(int j=0; j<mat[0].length; j++) {
				if(mat[i][j] == 1 && !visitor[i][j]) {
					bfs(mat, i, j, visitor);
					answer++;
				}
			}
		}
		return answer;
	}
	
	public void bfs(int[][] mat, int x, int y, boolean[][] visitor) {
		//시작점
		Queue<Node> q = new LinkedList<Node>();
		q.add(new Node(x, y, 1));
		visitor[x][y] = true;
		
		while(!q.isEmpty()) {
			Node node = q.poll();
			
			for(int i=0; i<4; i++) {
				int xx = node.x + dx[i];
				int yy = node.y + dy[i];
				if(xx >= 0 && xx < mat.length && yy >= 0 && yy < mat[0].length) {
					if(mat[xx][yy] == 1 && !visitor[xx][yy]) {
						visitor[xx][yy] = true;
						q.add(new Node(xx, yy, node.depth+1));
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] mat = {
				{0, 0, 0, 1, 1},
				{0, 1, 0, 0, 0},
				{1, 1, 1, 1, 0},
				{0, 1, 1, 0, 0},
				{0, 0, 0, 1, 1},
				{0, 0, 1, 1, 1}				
		};
		Solution2 sol = new Solution2();
		System.out.println(sol.solution(mat));
	}
	
	class Node {
		int x;
		int y;
		int depth;
		
		Node(int x, int y, int depth) {
			this.x = x;
			this.y = y;
			this.depth = depth;
		}
	}

}
