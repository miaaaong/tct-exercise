package sync;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {

	static ReentrantLock lock = new ReentrantLock();
	
	public static void main(String[] args) throws InterruptedException {
		lock = new ReentrantLock();
		TestRunnable r1 = new TestRunnable(1, lock);
		TestRunnable r2 = new TestRunnable(2, lock);
		
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		
		t1.start();
		t2.start();
		
		lock.lock();
		System.out.println("Main");
		for(int i=1; i<31; i++) {
			System.out.print(i + " ");
		}
		System.out.println("");
		lock.unlock();
		
		t1.join();
		t2.join();
		
	}

}
