package exercise;

public class VendingMachine {

	//자판기에서 물건을 살 때, 현재 가지고 있는 동전을 최대한 많이 사용할 수 있는 조합 찾기
	public static void main(String[] args) {
		int price = 212;
		int[] coins = {4, 2, 4, 4, 3, 4};
		int[] values = {500, 100, 50, 10, 5, 1};
		calculate(coins, values, price);
	}

	public static void calculate(int[] coins, int[] values, int price) {
		//동전을 최대한 많이 사용 == 구매 후 남는 동전 개수가 가장 적은 조합
		
		int total = 0;
		for(int i=0; i<coins.length; i++) {
			total += (coins[i]*values[i]);
		}
		int change = total - price;
		
		//동전 액면가가 큰 순서대로 정렬되어 있다고 치고...
		//금액이 큰 동전부터 남긴다
		int index = 0;
		while(change > 0) {
			if(coins[index] > 0 && change >= values[index]) {
				//현재 금액의 동전이 1개 이상 있고, 거스름돈이 현재 금액 이상이면 차감
				change -= values[index];
				coins[index]--;
			} else {
				//현재 금액의 동전이 없거나, 거스름돈이 현재 금액보다 작으면 다음 금액 단위로 넘어감
				index++;
			}
		}
		
		//coins에는 이제 물건을 사기 위해 쓸 동전 개수가 들어있음
		for(int i=0; i<coins.length; i++) {
			System.out.println(values[i] + "원 : " + coins[i] + "개");
		}
	}
}
