package com.lgcns.test;

import java.util.concurrent.BlockingQueue;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class InputRequestRunner implements Runnable {
	
	private int queueNum;
	private String inputURI = null;
	private final BlockingQueue<Input> inputQueue;
	
	public InputRequestRunner(int queueNum, String inputURI, BlockingQueue<Input> inputQueue) {
		this.queueNum = queueNum;
		this.inputURI = inputURI;
		this.inputQueue = inputQueue;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				HttpClient httpClient = new HttpClient();
//				System.out.println("##################");
//				System.out.println("Input Request : " + queueNum + ", " + System.currentTimeMillis());
//				System.out.println("##################");

				// 1. Input Queue에 데이터 요청
				httpClient.start();
				ContentResponse contentResponse = httpClient.newRequest(this.inputURI)
						.method(HttpMethod.GET).send();
				String responseBody = contentResponse.getContentAsString();
				
				Gson gson = new Gson();
				JsonObject jsonObj = gson.fromJson(responseBody, JsonObject.class);
				long timestamp = jsonObj.get("timestamp").getAsLong();
				String value = jsonObj.get("value").getAsString();
				
				httpClient.stop();
				
				// 2. Input queue에 추가 - 이후 workersthread에서 꺼내 worker로 처리
				Input input = new Input(queueNum, timestamp, value);
				inputQueue.put(input);
				
			} catch (Exception e) {
//				e.printStackTrace();
			}
			
		}

	}

}
