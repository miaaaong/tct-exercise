package com.lgcns.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class WorkersProcess {
	
	public static void main(String[] args) {
		int myProcessNum = Integer.parseInt(args[0]);
		int processCount = Integer.parseInt(args[1]);
		int threadCount = Integer.parseInt(args[2]);
		int totalQueueCount = Integer.parseInt(args[3]);
		String inputURIsStr = args[4];
		int outputQueueBatchSize = Integer.parseInt(args[5]);
		String outputURI = args[6];
		
		inputURIsStr = inputURIsStr.substring(1, inputURIsStr.length()-1);
		String[] inputURIs = inputURIsStr.split(",");
		
		run(myProcessNum, processCount, threadCount, totalQueueCount, inputURIs, outputQueueBatchSize, outputURI);
	}

	public static void run(int myProcessNum, int processCount, int threadCount, int totalQueueCount
			, String[] inputURIs, int outputQueueBatchSize, String outputURI) {
		//output queue 생성
		BlockingQueue<Output> outputQueue = new LinkedBlockingQueue<>();

		Thread t = null;
		// threads
		for(int myThreadNum=0; myThreadNum<threadCount; myThreadNum++) {
			// thread for worker
			WorkersThread workersThread = new WorkersThread(myProcessNum, processCount, myThreadNum, threadCount, totalQueueCount, inputURIs, outputQueue);
			t = new Thread(workersThread);
			t.start();
		}
		
//		// output sender - queue consumer 
//		OutputSender outputSender = new OutputSender(outputQueueBatchSize, outputURI, outputQueue);
//		t = new Thread(outputSender);
//		t.start();
		
		// output queue monitoring
		while(true) {
			if(outputQueue.size() >= outputQueueBatchSize) {
				// 기준 이상 모였으면 전송
				sendResults(outputQueue, outputURI);
			} else {
				// 첫번째 항목이 2초가 지났으면 모두 전송
				if(outputQueue.size() > 0) {
					long now = System.currentTimeMillis();
					if(now - outputQueue.peek().getCreatedTime() > 2000L) {
						sendResults(outputQueue, outputURI);
					}
				}
			}
		}
	}
	
	// output queue로 결과 전송
	private static void sendResults(BlockingQueue<Output> outputQueue, String outputURI) {
		List<Output> result = new ArrayList<Output>();
		outputQueue.drainTo(result);
		
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		for(Output output : result) {
			jsonArray.add(output.getResult());
		}
		JsonObject jsonObj = new JsonObject();
		jsonObj.add("result", jsonArray);
		String resultJsonStr = gson.toJson(jsonObj);
		
		try {
			HttpClient httpClient = new HttpClient();
			httpClient.start();
			
			Request request = httpClient.newRequest(outputURI).method(HttpMethod.POST);
			request.header(HttpHeader.CONTENT_TYPE, "application/json");
			request.content(new StringContentProvider(resultJsonStr, "utf-8"));
			request.send();
			
			httpClient.stop();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
}
