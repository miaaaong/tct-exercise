package pilot.q1;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String a = "ababc";
//		String b = "abcdab";

		String a = "abcabc";
		String b = "abcdab";
		
		Solution s = new Solution();
		System.out.println(s.solution(a, b));
		
	}
	
	public int solution(String s1, String s2) {
		int p1 = getMaxPartLength(s1, s2);
		int p2 = getMaxPartLength(s2, s1);
		
		int answer = s1.length() + s2.length() - Math.max(p1, p2);
		return answer;
	}
	
	private int getMaxPartLength(String s1, String s2) {
		int max = Math.min(s1.length(), s2.length());
		int result = 0;
		for(int i=1; i<=max; i++) {
			String p1 = s1.substring(s1.length()-i, s1.length());
			String p2 = s2.substring(0, i);
			if(p1.equals(p2)) {
				result = i;
			}
		}
		return result;
	}

}
