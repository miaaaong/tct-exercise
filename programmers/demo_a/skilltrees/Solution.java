package programmers.demo_a.skilltrees;

public class Solution {
    public int solution(String skill, String[] skill_trees) {
        int answer = 0;
        
        String exp = "[^" + skill + "]";
        for(String val : skill_trees) {
        	String input = val.replaceAll(exp, "");
        	int length = input.length();
        	if(skill.substring(0, length).equals(input)) {
        		answer++;
        	}
        }
        System.out.println(answer);
        return answer;
    }
    
    public static void main(String[] args) {
    	Solution solution = new Solution();
    	String skill = "CBD";
    	String[] skill_trees = {"BACDE", "CBADF", "AECB", "BDA"};
    	solution.solution(skill, skill_trees);
    	
    }
}
