package pilot.q3;

public class Solution {

	public static void main(String[] args) {
		System.out.println(solution(6));
	}

    public static int solution(int n) {
        int answer = getCount(n);
        return answer;
    }

	private static int getCount(int days) {
		int egg0 = 1;
		int egg1 = 0;
		int parent1 = 0;
		int parent2 = 0;
		int parent3 = 0;
		int parent4 = 0;
		int nothing = 0;
		
		for(int i=1; i<=days; i++) {
			nothing += parent4;
			parent4 = parent3;
			parent3 = parent2;
			parent2 = parent1;
			parent1 = egg1;
			egg1 = egg0;
			egg0 = parent1 + parent2 + parent3 + parent4;
		}
		return egg0 + egg1 + parent1 + parent2 + parent3 + parent4 + nothing;
	}

}
