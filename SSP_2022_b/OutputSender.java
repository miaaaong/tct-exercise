package com.lgcns.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class OutputSender implements Runnable {

	private int outputQueueBatchSize;
	private String outputURI;
	private final BlockingQueue<Output> outputQueue;
	
	public OutputSender(int outputQueueBatchSize, String outputURI, BlockingQueue<Output> outputQueue) {
		this.outputQueueBatchSize = outputQueueBatchSize;
		this.outputURI = outputURI;
		this.outputQueue = outputQueue;
	}
	
	@Override
	public void run() {
		while(true) {
			if(this.outputQueue.size() >= this.outputQueueBatchSize) {
				sendResults();
			} else {
				if(this.outputQueue.size() > 0) {
					long now = System.currentTimeMillis();
					if(now - this.outputQueue.peek().getCreatedTime() > 2000L) {
						sendResults();
					}
				}
			}
		}
	}
	
	private void sendResults() {
		List<Output> result = new ArrayList<Output>();
		this.outputQueue.drainTo(result);
		
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
			
			Request request = httpClient.newRequest(this.outputURI).method(HttpMethod.POST);
			request.header(HttpHeader.CONTENT_TYPE, "application/json");
			request.content(new StringContentProvider(resultJsonStr, "utf-8"));
			request.send();
			
			httpClient.stop();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
