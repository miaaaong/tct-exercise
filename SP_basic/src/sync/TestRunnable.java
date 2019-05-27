package sync;

import java.util.concurrent.locks.ReentrantLock;

public class TestRunnable implements Runnable {
	
	private int index = 0;
	private ReentrantLock lock;
	
	public TestRunnable(int index, ReentrantLock lock) {
		this.index = index;
		this.lock = lock;
	}

	@Override
	public void run() {
		lock.lock();
		System.out.println("Thread" + index);
		for(int i=1; i<31; i++) {
			System.out.print(i+" ");
		}
		System.out.println("");
		lock.unlock();
	}

}
