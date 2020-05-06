package programmers.level1.uniform;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 5;
		int[] lost = {1,2};
		int[] reserve = {2,3};
		System.out.println(solution(n, lost, reserve));
	}
	
	public static int solution(int n, int[] lost, int[] reserve) {
        int answer = 0;
        // 전체 학생 수
        answer = n;
        // 여벌이 있으나 도난당한 학생은 lost, reserve에서 제외 필요
        for(int i=0; i<lost.length; i++) {
        	for(int j=0; j<reserve.length; j++) {
        		if(lost[i] == reserve[j]) {
        			lost[i] = -1;
        			reserve[j] = -1;
        			break;
        		}
        	}
        }
        
        for(int i=0; i<lost.length; i++) {
        	boolean borrow = false;
        	for(int j=0; j<reserve.length && !borrow; j++) {
        		// 도난 상태가 -1이면 연산 안함 (lost, reserve에서 제외된 항목이므로)
        		if(lost[i] == -1) {
        			borrow = true;
        		} else if(lost[i] == reserve[j]) {
        			// 여벌이 사라져서 못 빌려 줌
        			borrow = true;
        			reserve[j] = -1;
        		} else if(lost[i] - reserve[j] == -1) {
        			// 뒷 번호에게 빌림
        			borrow = true;
        			reserve[j] = -1;
        		} else if(lost[i] - reserve[j] == 1) {
        			// 앞 번호에게 빌림
        			borrow = true;
        			reserve[j] = -1;
        		}
        	}
        	if(!borrow) {
        		// 못 빌렸으면 참석 못함
        		answer--;
        	}
        }
        return answer;
    }

}
