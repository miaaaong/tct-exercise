package sample.timer;

import java.util.Timer;
import java.util.TimerTask;

public class TimeoutTask extends TimerTask {
	
	private String queueName;
	private int maxFail;
	private Timer timer;
	
	public TimeoutTask(String queueName, int maxFail, Timer timer) {
		// task�� �ʿ��� ���� ����
		this.queueName = queueName;
		this.maxFail = maxFail;
		this.timer = timer;
	}

	@Override
	public void run() {
		// TODO ó�� ����
		System.out.println("timeout task");
		// task ���� �� �ʿ��ϸ� timer�� ����(���ϸ� timer�� ��� ����ϴ� ��)
		timer.cancel();
	}

}
