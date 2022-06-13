package com.lgcns.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class QueueServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Map<String, BlockingQueue<Message>> queueMap = new HashMap<String, BlockingQueue<Message>>();
	Map<String, Integer> procTimeoutMap = new HashMap<String, Integer>();
	Map<String, Integer> maxFailCntMap = new HashMap<String, Integer>();
	Map<String, Integer> waitTimeMap = new HashMap<String, Integer>();
	
	Map<String, BlockingQueue<Message>> dlQueueMap = new HashMap<String, BlockingQueue<Message>>();
	
	// process timeout timer
	Map<String, Timer> timerMap = new HashMap<String, Timer>();
	
	// fail count
	Map<String, Integer> msgFailCountMap = new HashMap<String, Integer>();
	
	Map<String, BlockingQueue<HttpServletRequest>> waitingQueueMap = new HashMap<String, BlockingQueue<HttpServletRequest>>();
	
	// GET 처리
	protected void doGet(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		System.out.println("doGet - " + requestURI);
		if (requestURI.startsWith("/RECEIVE")) {
			receiveMessage(req, res);
		} else if (requestURI.startsWith("/DLQ")) {
			receiveDeadLetter(req, res);
		}
	}

	// POST 처리
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		System.out.println("doPost - " + requestURI);
		if (requestURI.startsWith("/CREATE")) {
			createQueue(req, res);
		} else if (requestURI.startsWith("/SEND")) {
			sendMessage(req, res);
		} else if (requestURI.startsWith("/ACK")) {
			completeHandling(req, res);
		} else if (requestURI.startsWith("/FAIL")) {
			failHandling(req, res);
		}
	}
	
	private void createQueue(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		String[] split = requestURI.split("/");
		String queueName = split[2];

		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();

		if(queueMap.containsKey(queueName)) {
			jsonObject.addProperty("Result", "Queue Exist");
			
		} else {
			String strBody = getJsonBodyFromRequest(req);
			
			JsonObject jsonObj = gson.fromJson(strBody, JsonObject.class);
			int queueSize = jsonObj.get("QueueSize").getAsInt();
			int procTimeout = jsonObj.get("ProcessTimeout").getAsInt();
			int maxFailCnt = jsonObj.get("MaxFailCount").getAsInt();
			int waitTime = jsonObj.get("WaitTime").getAsInt();
			
			BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>(queueSize);
			queueMap.put(queueName, messageQueue);
			
			procTimeoutMap.put(queueName, procTimeout);
			maxFailCntMap.put(queueName, maxFailCnt);
			waitTimeMap.put(queueName, waitTime);
			
			jsonObject.addProperty("Result", "Ok");
		}
		
		String jsonStr = gson.toJson(jsonObject);
		res.setStatus(200);
		try {
			res.getWriter().write(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendMessage(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		String[] split = requestURI.split("/");
		String queueName = split[2];
		
		BlockingQueue<Message> queue = queueMap.get(queueName);
		
		String strBody = getJsonBodyFromRequest(req);		
		Gson gson = new Gson();
		JsonObject jsonObj = gson.fromJson(strBody, JsonObject.class);
		String msgValue = jsonObj.get("Message").getAsString();
		
		Message message = new Message(queueName, msgValue);
		
		JsonObject jsonObject = new JsonObject();
		boolean isOfferd = queue.offer(message);
		if(isOfferd) {
			notifyWaitingRequest(queueName);
			jsonObject.addProperty("Result", "Ok");			
		} else {
			jsonObject.addProperty("Result", "Queue Full");
		}
		String jsonStr = gson.toJson(jsonObject);
		res.setStatus(200);
		try {
			res.getWriter().write(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void receiveMessage(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		String[] split = requestURI.split("/");
		String queueName = split[2];
		
		Message msgToReturn = null;
		BlockingQueue<Message> queue = queueMap.get(queueName);
		Iterator<Message> iterator = queue.iterator();
		while(iterator.hasNext()) {
			Message message = iterator.next();
			if(message.isSendable()) {
				message.setSendable(false);
				msgToReturn = message;
				break;
			}
		}
		
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		if(msgToReturn == null) {
			// 전송할 메시지가 없는 경우
			Integer waitTime = waitTimeMap.get(queueName);
			if(waitTime == 0) {
				// 대기 시간 없으면 바로 결과 리턴
				System.out.println("대기 시간 없어서 바로 no message 리턴 " + queueName);
				jsonObject.addProperty("Result", "No Message");
			} else {
				// 대기
				try {
					synchronized (req) {
						long startTime = System.currentTimeMillis();
						
						BlockingQueue<HttpServletRequest> waitingQueue = waitingQueueMap.get(queueName);
						if(waitingQueue == null) {
							waitingQueue = new LinkedBlockingQueue<HttpServletRequest>();
							waitingQueueMap.put(queueName, waitingQueue);
						}
						waitingQueue.offer(req);
						
						// 대기
						req.wait(waitTime*1000);
						
						long endTime = System.currentTimeMillis();
						long dueTime = endTime - startTime;
						if(dueTime < waitTime*1000) {
							System.out.println("대기 시간 내 notify 발생 " + queueName + ", " + dueTime + ", " + waitTime*1000);
							// 리턴
							Message target = null;
							Iterator<Message> iter = queue.iterator();
							while(iter.hasNext()) {
								Message message = iter.next();
								if(message.isSendable()) {
									message.setSendable(false);
									target = message;
									break;
								}
							}
//							if(target != null) {
								jsonObject.addProperty("Result", "Ok");
								jsonObject.addProperty("MessageID", target.getId());
								jsonObject.addProperty("Message", target.getValue());
//							}							
						} else {
							System.out.println("대기 시간 만료 " + dueTime + ", " + waitTime*1000);
							jsonObject.addProperty("Result", "No Message");
						}
					}
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		} else {
			System.out.println("메시지 바로 리턴");
			jsonObject.addProperty("Result", "Ok");
			jsonObject.addProperty("MessageID", msgToReturn.getId());
			jsonObject.addProperty("Message", msgToReturn.getValue());
		}
		String jsonStr = gson.toJson(jsonObject);
		res.setStatus(200);
		try {
			res.getWriter().write(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(msgToReturn != null) {
			// process timeout
			Integer procTimeout = procTimeoutMap.get(queueName);
			if(procTimeout > 0) {
				Timer timer = new Timer();
				TimeoutTask task = new TimeoutTask(msgToReturn, timer, timerMap, msgFailCountMap, maxFailCntMap, dlQueueMap, waitingQueueMap);
				timer.schedule(task, procTimeout*1000);
				timerMap.put(msgToReturn.getId(), timer);
			}			
		}
		
	}
	
	private void completeHandling(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		String[] split = requestURI.split("/");
		String queueName = split[2];
		String messageId = split[3];
		
		// timer 해제
		if(timerMap.containsKey(messageId)) {
			Timer timer = timerMap.get(messageId);
			timer.cancel();
			timerMap.remove(messageId);
		}
		
		BlockingQueue<Message> queue = queueMap.get(queueName);
		Iterator<Message> iterator = queue.iterator();
		while(iterator.hasNext()) {
			Message message = iterator.next();
			if(message.getId().equals(messageId)) {
				queue.remove(message);
				break;
			}
		}
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Result", "Ok");
		String jsonStr = gson.toJson(jsonObject);
		res.setStatus(200);
		try {
			res.getWriter().write(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void failHandling(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		String[] split = requestURI.split("/");
		String queueName = split[2];
		String messageId = split[3];
		
		// timer 해제
		if(timerMap.containsKey(messageId)) {
			Timer timer = timerMap.get(messageId);
			timer.cancel();
			timerMap.remove(messageId);
		}
		
		int failCount = 1;
		if(msgFailCountMap.containsKey(messageId)) {
			failCount += msgFailCountMap.get(messageId);
		}
		Integer maxFail = maxFailCntMap.get(queueName);
		if(failCount > maxFail) {
			// move to dead letter queue
			Message msgToMove = null;
			BlockingQueue<Message> queue = queueMap.get(queueName);
			Iterator<Message> iterator = queue.iterator();
			while(iterator.hasNext()) {
				Message message = iterator.next();
				if(message.getId().equals(messageId)) {
					queue.remove(message);
					msgToMove = message;
					break;
				}
			}
			
			if(!dlQueueMap.containsKey(queueName)) {
				BlockingQueue<Message> dlQueue = new LinkedBlockingQueue<>();
				dlQueueMap.put(queueName, dlQueue);
			}
			BlockingQueue<Message> dlQueue = dlQueueMap.get(queueName);
			dlQueue.offer(msgToMove);
			
		} else {
			// update count
			msgFailCountMap.put(messageId, failCount);
			
			// 복원
			BlockingQueue<Message> queue = queueMap.get(queueName);
			Iterator<Message> iterator = queue.iterator();
			while(iterator.hasNext()) {
				Message message = iterator.next();
				if(message.getId().equals(messageId)) {
					message.setSendable(true);
					notifyWaitingRequest(queueName);
					break;
				}
			}	
		}
		
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Result", "Ok");
		String jsonStr = gson.toJson(jsonObject);
		res.setStatus(200);
		try {
			res.getWriter().write(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void receiveDeadLetter(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		String[] split = requestURI.split("/");
		String queueName = split[2];
		
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();

		if(dlQueueMap.containsKey(queueName)) {
			BlockingQueue<Message> queue = dlQueueMap.get(queueName);
			Message message = queue.poll();
			if(message == null) {
				jsonObject.addProperty("Result", "No Message");			
			} else {
				jsonObject.addProperty("Result", "Ok");
				jsonObject.addProperty("MessageID", message.getId());
				jsonObject.addProperty("Message", message.getValue());
			}			
		} else {
			jsonObject.addProperty("Result", "No Message");
		}
		
		String jsonStr = gson.toJson(jsonObject);
		res.setStatus(200);
		try {
			res.getWriter().write(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}	
	
	private String getJsonBodyFromRequest(HttpServletRequest req) {
		String strBody = "";
		try (InputStreamReader isr = new InputStreamReader(req.getInputStream());
				BufferedReader br = new BufferedReader(isr)) {
			String buffer;
			StringBuilder sb = new StringBuilder();
			while ((buffer = br.readLine()) != null) {
				sb.append(buffer + "\n");
			}
			strBody = sb.toString();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return strBody;
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
