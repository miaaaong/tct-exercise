package sample.messagebroker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main_Service {

	public static void main(String[] args) throws InterruptedException {
		// producer가 담고 consumer가 읽을 queue 생성
		BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>();
		
		// 적당한 수의 thread 개수 설정
		int threadNum = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threadNum);

		// 특정 조건에 thread를 종료하기 위해 담아둘 변수
		List<Consumer> consumers = new ArrayList<>();
		List<Producer> producers = new ArrayList<>();
		
		for(int i=0; i<threadNum-2; i++) {
			// 공동으로 사용할 queue를 넘겨서 consumer 생성
			Consumer con = new Consumer(sharedQueue);
			// 향후 제어하기 위해 저장
			consumers.add(con);
			
			// consumer 실행
			service.execute(con);
		}
		
		for(int i=0; i<2; i++) {
			// 공동으로 사용할 queue를 넘겨서 producer 생성
			Producer pro = new Producer(sharedQueue);
			// 향후 제어하기 위해 저장
			producers.add(pro);
			
			// producer 실행
			service.execute(pro);
		}
		
		// TODO 로직 구현
		Thread.sleep(5000);
		
		// 특정 상황이 되면 producer 종료
		for(Producer pro : producers) {
			pro.stop();
		}
		System.out.println("producers are stopped");

		// 특정 상황이 되면 consumer 종료
		for(Consumer con : consumers) {
			con.stop();
		}
		System.out.println("consumers are stopped");
		
		// thread pool 종료 - 무한 실행하는 thread가 있으면 종료 안됨...
		service.shutdown();
		
	}

}
