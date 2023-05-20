package com.lgcns.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RunManager {

	public static void main(String[] args) {
		try {
			// 1. Controller·ÎºÎÅÍ ÇÊ¼ö Á¤º¸ È¹µæ
			HttpClient httpClient = new HttpClient();
			httpClient.start();
			ContentResponse contentResponse = httpClient.newRequest("http://127.0.0.1:8080/queueInfo")
					.method(HttpMethod.GET).send();
			String responseBody = contentResponse.getContentAsString();
			
			Gson gson = new Gson();
			JsonObject jsonObj = gson.fromJson(responseBody, JsonObject.class);
			int processCount = jsonObj.get("processCount").getAsInt();
			String threadCount = jsonObj.get("threadCount").getAsString();
			String outputQueueBatchSize = jsonObj.get("outputQueueBatchSize").getAsString();
			String totalQueueCount = jsonObj.get("inputQueueCount").getAsString();
			JsonArray inputURIs = jsonObj.get("inputQueueURIs").getAsJsonArray();
			String inputURIsStr = gson.toJson(inputURIs); 
			String outputURI = jsonObj.get("outputQueueURI").getAsString();
			
			httpClient.stop();
			
			// 2. È¹µæÇÑ Á¤º¸·Î subprocess »ý¼º ¹× ½ÇÇà
			for(int myProcessNum=0; myProcessNum<processCount; myProcessNum++) {
				Class clazz = WorkersProcess.class;
				
				String javaHome = System.getProperty("java.home");
			    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
			    String classpath = System.getProperty("java.class.path");

				List<String> arguments = new ArrayList<String>();
				arguments.add(String.valueOf(myProcessNum));
				arguments.add(String.valueOf(processCount));
				arguments.add(threadCount);
				arguments.add(totalQueueCount);
				arguments.add(inputURIsStr);
				arguments.add(outputQueueBatchSize);
				arguments.add(outputURI);
				
			    List<String> command = new ArrayList<>();
			    command.add(javaBin);
//			    command.addAll(jvmArgs);
			    command.add("-cp");
			    command.add(classpath);
			    command.add(clazz.getName());
			    command.addAll(arguments);

			    ProcessBuilder builder = new ProcessBuilder(command);
			    Process process = builder.inheritIO().start();
			    
			    Thread.sleep(500);
//			    Process process = builder.start();
//			    process.waitFor();
//			    System.out.println(process.exitValue());
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
