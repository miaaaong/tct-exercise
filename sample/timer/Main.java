package sample.timer;

import java.util.Timer;
import java.util.TimerTask;

public class Main {

	public static void main(String[] args) {
		Timer timer = new Timer();
		// 1. parameter�� �ʿ� ������ �ٷ� ����
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// ó�� ����
				System.out.println("task!");
				// task�� ���� �� �ʿ��ϸ� timer�� ���� (���ϸ� timer�� ��� ����ִ� ��)
				// ����! - schedule �Ǿ��ִ� ��� task ����� 
				timer.cancel();
			}
		};
		int waitSeconds = 3;
		// ������ �ð� �� task ����
		timer.schedule(task, waitSeconds * 1000);
		// �ݺ��� ��쿡�� �ش� ���ݵ� ����
//		timer.schedule(task, waitSeconds * 1000, 5000);
		
		boolean someCondition = false;
		if(someCondition) {
			// Ư�� ������ �����ϴ� ��� timer ���
			timer.cancel();
		}
		
		// 2. parameter�� �ʿ��ϸ� TimerTask�� ����Ͽ� ����
		Timer timeoutTimer = new Timer();
		TimeoutTask timeoutTask = new TimeoutTask("queue", 3, timeoutTimer);
		long delay = waitSeconds * 1000;
		// ������ �ٸ� timer�� schedule �ص� ��... ��ǿ� ���� timer�� �߰� �����ϵ� ���� ���� ����ϵ� �ϸ� �� ��
		timeoutTimer.schedule(timeoutTask, delay);
		
	}

}
