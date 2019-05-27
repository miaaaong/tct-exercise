package thread;

public class TestRunnable implements Runnable {
	
	private int index = 0;
	
	public TestRunnable(int index) {
		this.index = index;
	}

	@Override
	public void run() {
		for(int i=0; i<10; i++) {
			System.out.println("[Thread" + index + "] " + i);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
