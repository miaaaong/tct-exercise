package com.lgcns.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main_Service {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>();
		
		int threadNum = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threadNum);

		List<Consumer> consumers = new ArrayList<>();
		List<Producer> producers = new ArrayList<>();
		
		for(int i=0; i<threadNum-2; i++) {
			Consumer con = new Consumer(sharedQueue);
			consumers.add(con);
			
			service.execute(con);
		}
		
		for(int i=0; i<2; i++) {
			Producer pro = new Producer(sharedQueue);
			producers.add(pro);
			
			service.execute(pro);
		}
		
		Thread.sleep(5000);
		
		for(Producer pro : producers) {
			pro.stop();
		}
		
		System.out.println("producers are stopped");
		
		for(Consumer con : consumers) {
			con.stop();
		}
		
		System.out.println("consumers are stopped");
		
		service.shutdown();
		
	}

}
