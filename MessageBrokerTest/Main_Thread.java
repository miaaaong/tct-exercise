package com.lgcns.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main_Thread {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>();
		
		int threadNum = 4;

		List<Consumer> consumers = new ArrayList<>();
		List<Producer> producers = new ArrayList<>();
		
		for(int i=0; i<threadNum-2; i++) {
			Consumer con = new Consumer(sharedQueue);
			consumers.add(con);
			
			Thread t = new Thread(con);
			t.start();
		}
		
		for(int i=0; i<2; i++) {
			Producer pro = new Producer(sharedQueue);
			producers.add(pro);
			
			Thread t = new Thread(pro);
			t.start();
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
		
	}

}
