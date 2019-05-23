package com.lgcns.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable {
	
	private final BlockingQueue<String> sharedQueue;
	private AtomicBoolean running = new AtomicBoolean(true);

	public Consumer(BlockingQueue<String> queue) {
		this.sharedQueue = queue;
	}
	
	public void stop() {
		this.running = new AtomicBoolean(false);
	}

	@Override
	public void run() {
		while(running.get() || !sharedQueue.isEmpty()) {
			// poll - 없으면 null 반환
			String message = sharedQueue.poll();
			if(message == null && !running.get()) {
				//while문 조건을 만족했지만 그 사이에 queue가 비어버린 경우, stop 지시가 있었으면 종료 필요
				break;
			} else if(message != null) {
				System.out.println("Consumed: " + message);
			}
			
			// take - 없으면 생길 때까지 대기
//			try {
//				String message = sharedQueue.take();
//				System.out.println("Consumed: " + message);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		System.out.println("stop consumer!");
	}

}
