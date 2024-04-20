package com.lgcns.test.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadTermination {
	
	// ÂüÁ¶ 
	// https://www.pkslow.com/archives/wait-for-threads-to-finish-en
	// https://stackoverflow.com/questions/1250643/how-to-wait-for-all-threads-to-finish-using-executorservice

	private static int THREADS_NUM = 10;
	private static int NUM = 4;

	private static void threadJoin() {
		List<Thread> threads = new ArrayList<Thread>();

		for (int i = 0; i < NUM; i++) {
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("Task");
				}
			});
			t.start();
			threads.add(t);
		}
		
		threads.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private static void executeServiceIsTerminated() {
		ExecutorService executorService = Executors.newFixedThreadPool(THREADS_NUM);

		for (int i = 0; i < NUM; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("Task");
				}
			});
		}

		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
