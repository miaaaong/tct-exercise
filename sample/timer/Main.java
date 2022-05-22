package sample.timer;

import java.util.Timer;
import java.util.TimerTask;

public class Main {

	public static void main(String[] args) {
		Timer timer = new Timer();
		// 1. parameter가 필요 없으면 바로 생성
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// 처리 로직
				System.out.println("task!");
				// task를 실행 후 필요하면 timer도 종료 (안하면 timer가 계속 살아있는 듯)
				// 주의! - schedule 되어있던 모든 task 종료됨 
				timer.cancel();
			}
		};
		int waitSeconds = 3;
		// 정해진 시간 뒤 task 실행
		timer.schedule(task, waitSeconds * 1000);
		// 반복할 경우에는 해당 간격도 설정
//		timer.schedule(task, waitSeconds * 1000, 5000);
		
		boolean someCondition = false;
		if(someCondition) {
			// 특정 조건을 만족하는 경우 timer 취소
			timer.cancel();
		}
		
		// 2. parameter가 필요하면 TimerTask를 상속하여 구현
		Timer timeoutTimer = new Timer();
		TimeoutTask timeoutTask = new TimeoutTask("queue", 3, timeoutTimer);
		long delay = waitSeconds * 1000;
		// 기존의 다른 timer에 schedule 해도 됨... 요건에 따라 timer를 추가 생성하든 기존 것을 사용하든 하면 될 듯
		timeoutTimer.schedule(timeoutTask, delay);
		
	}

}
