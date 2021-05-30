package sample.messagebroker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Producer implements Runnable {
	
	private final BlockingQueue<String> sharedQueue;
	private AtomicBoolean running = new AtomicBoolean(true);
	
	public Producer(BlockingQueue<String> queue) {
		this.sharedQueue = queue;
	}
	
	public void stop() {
		this.running = new AtomicBoolean(false);
	}

	@Override
	public void run() {
		// stop() 실행으로 false이기 전까지는 계속 반복
		while(running.get()) {
			String message = "Time: " + System.currentTimeMillis();
			try {
				sharedQueue.put(message);
				System.out.println("Produced: " + message);
				Thread.sleep(50);
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
		}
		System.out.println("stop producer!");
	}

}
