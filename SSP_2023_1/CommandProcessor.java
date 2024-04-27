package com.lgcns.test;

import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CommandProcessor implements Runnable {
	
	private Device device;
	private String[] forwardCommands;
	private String firstParam;
	private Map<String, String> results;
	
	public CommandProcessor(Device device, String[] forwardCommands, String firstParam, Map<String, String> results) {
		this.device = device;
		this.forwardCommands = forwardCommands;
		this.firstParam = firstParam;
		this.results = results;
	}

	@Override
	public void run() {
		String result = null;
		String param = firstParam;
		String url = device.getHostName() + ":" + device.getPort();
		
		// forward command 순차 처리 
		for(String command: forwardCommands) {
			// 실행 가능 여부 확인
			while(!ProcessManager.canProcessNow(device, command)) {
				// 실행 불가하면 일정 시간 대기 후 다시 확인
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
			// 실행
			Gson gson = new Gson();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("command", command);
			jsonObject.addProperty("param", param);
			String body = gson.toJson(jsonObject);
			
			HttpClient httpClient = new HttpClient();
			try {
				httpClient.start();
				Request request = httpClient.newRequest("http://" + url +"/fromEdge").method(HttpMethod.POST);
				request.header(HttpHeader.CONTENT_TYPE, "application/json");
				request.content(new StringContentProvider(body, "utf-8"));
				ContentResponse contentResponse = request.send();
				result = JsonParser.parseString(contentResponse.getContentAsString()).getAsJsonObject().get("result").getAsString();
				httpClient.stop();
				
				// 현재 결과가 다음 커맨드의 파라미터로 사용됨
				param = result; 
				// 프로세스 완료 처리
				ProcessManager.completeProcess(device, command);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
		// 결과에 디바이스명과 함께 저장
		results.put(device.getDeviceName(), result);
	}

}
