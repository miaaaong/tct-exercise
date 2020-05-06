package programmers.demo_a.palindrome2;

public class Solution {

    public int solution(String s) {
        int answer = 1;
        
        // 2
        for(int i=0; i<s.length()-1; i++) {
        	int max = getMaxPalindrome(s, i, i+1);
        	answer = Math.max(answer, max);
        }
        
        // 3
        for(int i=0; i<s.length()-2; i++) {
        	int max = getMaxPalindrome(s, i, i+2);
        	answer = Math.max(answer, max);
        }        
        return answer;
    }
    
    private int getMaxPalindrome(String input, int start, int end) {
    	int max = 0;
    	int length = input.length();
    	while(start >=0 && end < length) {
    		if(input.charAt(start) == input.charAt(end)) {
    			max = end - start + 1;
    			start--;
    			end++;
    		} else {
    			break;
    		}
    	}
    	return max;
    }
    
    public static void main(String[] args) {
    	String s= "abacde";
    	Solution solultion = new Solution();
    	System.out.println(solultion.solution(s));
    	
	}

}
