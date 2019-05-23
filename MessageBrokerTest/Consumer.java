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
			// poll - ������ null ��ȯ
			String message = sharedQueue.poll();
			if(message == null && !running.get()) {
				//while�� ������ ���������� �� ���̿� queue�� ������ ���, stop ���ð� �־����� ���� �ʿ�
				break;
			} else if(message != null) {
				System.out.println("Consumed: " + message);
			}
			
			// take - ������ ���� ������ ���
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
