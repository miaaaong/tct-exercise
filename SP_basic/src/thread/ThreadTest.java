package thread;

public class ThreadTest {

	public static void main(String[] args) throws InterruptedException {
		TestRunnable r1 = new TestRunnable(1);
		TestRunnable r2 = new TestRunnable(2);
		
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		
		t1.start();
		t2.start();
		
		for(int i=0; i<10; i++) {
			System.out.println("[Main] " + i);
			Thread.sleep(5);
		}
		
		t1.join();
		t2.join();
	}

}
