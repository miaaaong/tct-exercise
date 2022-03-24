package bfs.maze;

import java.util.LinkedList;
import java.util.Queue;

public class Solution {

	private static int[] dx = {0, 0, -1, 1};
	private static int[] dy = {-1, 1, 0, 0};	

	// (0,0)에서 (n,m)으로 도달하는 최단 경로
	public int solution(int[][] mat) {
		boolean[][] visitor = new boolean[mat.length][mat[0].length];
		return bfs(mat, 0, 0, visitor);
	}
	
	public int bfs(int[][] mat, int x, int y, boolean[][] visitor) {
		Queue<Node> q = new LinkedList<Solution.Node>();
		q.offer(new Node(x, y, 1));
		visitor[x][y] = true;
		
		while(!q.isEmpty()) {
			Node node = q.poll();
			if(node.x == mat.length-1 && node.y == mat[0].length-1) {
				return node.depth;
			}
			
			for(int i=0; i<4; i++) {
				int xx = node.x + dx[i];
				int yy = node.y + dy[i];
				if(xx >= 0 && yy >= 0 && xx < mat.length && yy < mat[0].length) {
					if(mat[xx][yy] == 1 && !visitor[xx][yy]) {
						visitor[xx][yy] = true;
						q.offer(new Node(xx, yy, node.depth+1));
					}
				}
			}
		}
		return -1;
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
		Solution sol = new Solution();
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
