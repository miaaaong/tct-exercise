package programmers.level3.tiling2;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Solution().solution(6));
	}

	public long solution(int N) {
        long answer = (getNumber(Integer.toUnsignedLong(N)) + getNumber(Integer.toUnsignedLong(N+1)))*2;
        return answer;
    }
	
//	public long getNumber(long n) {
//		if(n == 1 || n == 2) {
//			return 1L;
//		} else {
//			return getNumber(n-1) + getNumber(n-2);
//		}
//	}
	
	public long getNumber(long n) {
		if(n == 1 || n == 2) {
			return 1L;
		} else {
			long a = 1L;
			long b = 1L;
			long c = 0L;
			for(int i=3; i<=n; i++) {
				c = a+b;
				a = b;
				b = c;
			}
			return c;
		}
	}
}
