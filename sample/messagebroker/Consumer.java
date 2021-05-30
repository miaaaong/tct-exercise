package sample.messagebroker;

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
		// 실행 중일 때는 계속 반복
		// 실행 종료가 되었으나, 남은 내용이 있으면 계속 읽어야 함
		while(running.get() || !sharedQueue.isEmpty()) {
			// poll - queue에서 메시지 꺼냄, 없으면 null 리턴
			String message = sharedQueue.poll();
			if(message == null && !running.get()) {
				// 메시지가 없고, 실행 종료이면 while문 종료
				break;
			} else if(message != null) {
				// 메시지가 있으면 처리
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
