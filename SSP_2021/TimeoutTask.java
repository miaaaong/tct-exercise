package com.lgcns.test;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletRequest;

public class TimeoutTask extends TimerTask {
	
	private Message message;
	private Timer timer;
	private Map<String, Timer> timerMap;
	private Map<String, Integer> msgFailCountMap;
	private Map<String, Integer> maxFailCntMap;
	private Map<String, BlockingQueue<Message>> dlQueueMap;
	private Map<String, BlockingQueue<HttpServletRequest>> waitingQueueMap;
	
	public TimeoutTask(Message message, Timer timer, Map<String, Timer> timerMap, 
			Map<String, Integer> msgFailCountMap, Map<String, Integer> maxFailCntMap,
			Map<String, BlockingQueue<Message>> dlQueueMap,
			Map<String, BlockingQueue<HttpServletRequest>> waitingQueueMap) {
		this.message = message;
		this.timer = timer;
		this.timerMap = timerMap;
		this.msgFailCountMap = msgFailCountMap;
		this.maxFailCntMap = maxFailCntMap;
		this.dlQueueMap = dlQueueMap;
		this.waitingQueueMap = waitingQueueMap;
	}

	@Override
	public void run() {
		String messageId = this.message.getId();
		String queueName = message.getQueueName();
		
		int failCount = 1;
		if(this.msgFailCountMap.containsKey(messageId)) {
			failCount += msgFailCountMap.get(messageId);
		}
		
		Integer maxFail = maxFailCntMap.get(queueName);
		if(failCount > maxFail) {
			// move to dead letter queue
			Message msgToMove = this.message;
			
			if(!dlQueueMap.containsKey(queueName)) {
				BlockingQueue<Message> dlQueue = new LinkedBlockingQueue<>();
				dlQueueMap.put(queueName, dlQueue);
			}
			BlockingQueue<Message> dlQueue = dlQueueMap.get(queueName);
			dlQueue.offer(msgToMove);
			
		} else {
			// update count
			msgFailCountMap.put(messageId, failCount);
			
			// 전송 가능하도록 복원
			this.message.setSendable(true);
			notifyWaitingRequest(queueName);
		}
		
		// 타이머 종료
		this.timer.cancel();
		// map에서 타이머 정보 삭제
		this.timerMap.remove(this.message.getId());
		
	}
	
	private void notifyWaitingRequest(String queueName) {
		if(waitingQueueMap.containsKey(queueName)) {
			BlockingQueue<HttpServletRequest> queue = waitingQueueMap.get(queueName);
			HttpServletRequest request = queue.poll();
			if(request != null) {
				synchronized (request) {
					request.notifyAll();
				}
			}
		}
	}

}
