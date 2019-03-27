package exercise;

public class PrimeNumber {

	//소수 출력
	public static void main(String[] args) {
		PrimeNumber obj = new PrimeNumber();
		int max = 100;
		for(int i=2; i<=max; i++) {
			if(obj.isPrimeNumber(i)) {
				System.out.println(i + ", ");
			}
		}

	}

	public boolean isPrimeNumber(int num) {
		int max = num / 2;
		for(int i=2; i<=max; i++) {
			if(num%i == 0) {
				return false;
			}
		}
		return true;
	}
}
