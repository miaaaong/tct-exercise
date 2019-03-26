package algorithm.a08.mazestack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class A08_Maze1_Stack {

	public static void main(String[] args) throws FileNotFoundException {
		System.setIn(new FileInputStream("data/maze_input.txt"));		
		Scanner scanner = new Scanner(System.in);
		
		int caseNum = scanner.nextInt();
		for(int num=0; num<caseNum; num++) {			
			int rowCount = scanner.nextInt();
			int colCount = scanner.nextInt();
			int[][] input = new int[rowCount][colCount];
			for(int i=0; i<rowCount; i++) {
				int[] rowValue = new int[colCount];
				for(int j=0; j<colCount; j++) {
					rowValue[j] = scanner.nextInt();
				}
				input[i] = rowValue;
			}
			boolean result = hasPath(input);
			System.out.println("#" + (num + 1) + " " + (result ? "YES" : "NO"));
		}
		
		scanner.close();
	}
	
	private static boolean hasPath(int[][] input) {
		int rowLength = input.length;
		int colLengh = input[0].length;
		int[][] flag = new int[rowLength][colLengh];
		
		Stack<int[]> stack = new Stack<>();
		
		//출발점
		int row = 0;
		int col = 0;
		stack.push(new int[]{row,col});
		flag[row][col] = 1;
		
		while(true) {
			if(row+1<rowLength && flag[row+1][col] == 0 && input[row+1][col] == 0) {
				//right
				stack.push(new int[]{row+1,col});
				flag[row+1][col] = 1;
				row += 1;
			} else if(row-1>-1 && flag[row-1][col] == 0 && input[row-1][col] == 0) {
				//left
				stack.push(new int[]{row-1,col});
				flag[row-1][col] = 1;
				row -= 1;
			} else if(col-1>-1 && flag[row][col-1] == 0 && input[row][col-1] == 0) {
				//up
				stack.push(new int[]{row,col-1});
				flag[row][col-1] = 1;
				col -= 1;
			} else if(col+1<colLengh && flag[row][col+1] == 0 && input[row][col+1] == 0) {
				//down
				stack.push(new int[]{row,col+1});
				flag[row][col+1] = 1;
				col += 1;				
			} else {
				if(row == rowLength-1 && col == colLengh-1) {
					break;
				} else {					
					//back
					if(stack.size() == 1) {
						break;
					}
					stack.pop();
					int[] back = stack.peek();
					row = back[0];
					col = back[1];
				}
			}
		}
		
		if(row == rowLength-1 && col == colLengh-1) {
			return true;
		} else {
			return false;
		}
	}

}
