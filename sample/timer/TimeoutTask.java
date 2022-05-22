package sample.timer;

import java.util.Timer;
import java.util.TimerTask;

public class TimeoutTask extends TimerTask {
	
	private String queueName;
	private int maxFail;
	private Timer timer;
	
	public TimeoutTask(String queueName, int maxFail, Timer timer) {
		// task에 필요한 정보 설정
		this.queueName = queueName;
		this.maxFail = maxFail;
		this.timer = timer;
	}

	@Override
	public void run() {
		// TODO 처리 로직
		System.out.println("timeout task");
		// task 실행 후 필요하면 timer도 종료(안하면 timer는 계속 대기하는 듯)
		timer.cancel();
	}

}
