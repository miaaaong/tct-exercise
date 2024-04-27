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
		
		// forward command ���� ó�� 
		for(String command: forwardCommands) {
			// ���� ���� ���� Ȯ��
			while(!ProcessManager.canProcessNow(device, command)) {
				// ���� �Ұ��ϸ� ���� �ð� ��� �� �ٽ� Ȯ��
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
			// ����
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
				
				// ���� ����� ���� Ŀ�ǵ��� �Ķ���ͷ� ����
				param = result; 
				// ���μ��� �Ϸ� ó��
				ProcessManager.completeProcess(device, command);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
		// ����� ����̽���� �Բ� ����
		results.put(device.getDeviceName(), result);
	}

}
