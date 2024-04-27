package com.lgcns.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EdgeNodeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Map<String, Device> deviceInfo = null;
	private static Map<String, Map<String, String[]>> serverCommandInfo = null;
	private static Map<String, DeviceCommand> deviceCommandInfo = null;
	
	@Override
	public void init() throws ServletException {
		// load device info
		deviceInfo = new HashMap<String, Device>();
		String deviceFilePath = "./INFO/DEVICE.JSON"; 
		JsonObject jsonObject = null;
		try(FileReader fr = new FileReader(deviceFilePath);) {
			jsonObject = JsonParser.parseReader(fr).getAsJsonObject();
		} catch(Exception e) {
			e.printStackTrace();
		}
		JsonArray deviceTypeArray = jsonObject.get("deviceInfo").getAsJsonArray();
		for(int i=0; i<deviceTypeArray.size(); i++) {
			JsonObject deviceTypeObject = deviceTypeArray.get(i).getAsJsonObject();
			String type = deviceTypeObject.get("type").getAsString();
			int parallelProcessingCount = deviceTypeObject.get("parallelProcessingCount").getAsInt();
			JsonArray deviceList = deviceTypeObject.get("deviceList").getAsJsonArray();
			for(int j=0; j<deviceList.size(); j++) {
				JsonObject deviceObject = deviceList.get(j).getAsJsonObject();
				String deviceName = deviceObject.get("device").getAsString();
				String hostname = deviceObject.get("hostname").getAsString();
				String port = deviceObject.get("port").getAsString();
	
				Device device = new Device();
				device.setDeviceName(deviceName);
				device.setHostName(hostname);
				device.setParallelProcessingCount(parallelProcessingCount);
				device.setPort(port);
				device.setType(type);
				
				deviceInfo.put(deviceName, device);
			}
			
		}
		
		// load server command 
		serverCommandInfo = new HashMap<String, Map<String, String[]>>();
		String commandFilePath = "./INFO/SERVER_COMMAND.JSON"; 
		jsonObject = null;
		try(FileReader fr = new FileReader(commandFilePath);) {
			jsonObject = JsonParser.parseReader(fr).getAsJsonObject();
		} catch(Exception e) {
			e.printStackTrace();
		}
		JsonArray commandArray = jsonObject.get("serverCommandInfo").getAsJsonArray();
		for(int i=0; i<commandArray.size(); i++) {
			JsonObject commandObject = commandArray.get(i).getAsJsonObject();
			String command = commandObject.get("command").getAsString();
			JsonArray forwardCommandArray = commandObject.get("forwardCommandInfo").getAsJsonArray();
			Map<String, String[]> forwardCommandMap = new HashMap<String, String[]>();
			for(int j=0; j<forwardCommandArray.size(); j++) {
				
				JsonObject forwardCommandObject = forwardCommandArray.get(j).getAsJsonObject();
				String type = forwardCommandObject.get("type").getAsString();
				JsonArray array = forwardCommandObject.get("forwardCommand").getAsJsonArray();
				String[] commands = new String[array.size()];
				for(int k=0; k<array.size(); k++) {
					commands[k] = array.get(k).getAsString();
				}
				
				forwardCommandMap.put(type, commands);
			}
			serverCommandInfo.put(command, forwardCommandMap);
		}
		
		// load device command info
		deviceCommandInfo = new HashMap<String, DeviceCommand>();
		String deviceCommandFilePath = "./INFO/DEVICE_COMMAND.JSON"; 
		jsonObject = null;
		try(FileReader fr = new FileReader(deviceCommandFilePath);) {
			jsonObject = JsonParser.parseReader(fr).getAsJsonObject();
		} catch(Exception e) {
			e.printStackTrace();
		}
		JsonArray deviceCommandArray = jsonObject.get("deviceCommandInfo").getAsJsonArray();
		for(int i=0; i<deviceCommandArray.size(); i++) {
			JsonObject deviceCommandObject = deviceCommandArray.get(i).getAsJsonObject();
			String command = deviceCommandObject.get("command").getAsString();
			String forwardCommand = deviceCommandObject.get("forwardCommand").getAsString();
			String computingLib = deviceCommandObject.get("computingLib").getAsString();
			DeviceCommand deviceCommand = new DeviceCommand();
			deviceCommand.setForwardCommand(forwardCommand);
			deviceCommand.setComputingLib(computingLib);
			deviceCommandInfo.put(command, deviceCommand);
		}
		
	}
	
	// POST 처리
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		
		if(requestURI.startsWith("/fromServer")) {
			handleServerRequest(req, res);
		} else if(requestURI.startsWith("/fromDevice")) {
			handleDeviceRequest(req, res);
		}
	}
	
	private void handleDeviceRequest(HttpServletRequest req, HttpServletResponse res) {
		try {
			// request from device
			String requestBody = getJsonBodyFromRequest(req);
			
			Gson gson = new Gson();
			JsonObject jsonObj = gson.fromJson(requestBody, JsonObject.class);
			String command = jsonObj.get("command").getAsString();
			String parameter = jsonObj.get("param").getAsString();
			
			DeviceCommand deviceCommand = deviceCommandInfo.get(command);
			String computingLib = deviceCommand.getComputingLib();
			String forwardCommand = deviceCommand.getForwardCommand();
			
			String result = runLibrary(computingLib, parameter);
			if(result != null) {
				// forward to server
				gson = new Gson();
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("command", forwardCommand);
				jsonObject.addProperty("param", result);
				String body = gson.toJson(jsonObject);
				
				HttpClient httpClient = new HttpClient();
				try {
					httpClient.start();
					Request request = httpClient.newRequest("http://127.0.0.1:7010/fromEdge").method(HttpMethod.POST);
					request.header(HttpHeader.CONTENT_TYPE, "application/json");
					request.content(new StringContentProvider(body, "utf-8"));
					ContentResponse contentResponse = request.send();
					httpClient.stop();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			res.setStatus(200);
			res.getWriter().flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String runLibrary(String computingLib, String parameter) {
		String result = null;
		File file = new File("./LIB/" + computingLib + ".jar");
		URL url;
		try {
			url = file.toURI().toURL();
			URL[] urls = new URL[] { url };
			URLClassLoader cl = new URLClassLoader(urls);
			Class myclass = cl.loadClass("com.lgcns.computing.ComputingLib");
			Constructor constructor = myclass.getConstructor();
			Object instance = myclass.newInstance();
			Method method = myclass.getMethod("compute", String.class);
			Object invoke = method.invoke(instance, parameter);
			if(invoke != null) {
				result = (String)invoke;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private void handleServerRequest(HttpServletRequest req, HttpServletResponse res) {
		try {
			// request from server
			String requestBody = getJsonBodyFromRequest(req);
			Gson gson = new Gson();
			JsonObject jsonObj = gson.fromJson(requestBody, JsonObject.class);
			String requestCommand = jsonObj.get("command").getAsString();
			List<JsonElement> targetDevices = jsonObj.get("targetDevice").getAsJsonArray().asList();
			String parameter = jsonObj.get("param").getAsString();
			
			// forward request to device
			Map<String, String> results = new HashMap<String, String>();

			List<Thread> threadList = new ArrayList<Thread>();
			for(int i=0; i<targetDevices.size(); i++) {
				String deviceName = targetDevices.get(i).getAsString();
				Device device = deviceInfo.get(deviceName);
				String deviceType = device.getType();
				String[] forwardCommands = serverCommandInfo.get(requestCommand).get(deviceType);
				
				// 디바이스별로 병렬 실행
				CommandProcessor processor = new CommandProcessor(device, forwardCommands, parameter, results);
				Thread t = new Thread(processor);
				t.start();
				threadList.add(t);
			}
			
			// 모든 프로세스의 종료 대기
			for(Thread t : threadList) {
				t.join();
			}
			
			List<String> responseList = new ArrayList<String>();
			for(int i=0; i<targetDevices.size(); i++) {
				String deviceName = targetDevices.get(i).getAsString();
				String resultOfDevice = results.get(deviceName);
				responseList.add(resultOfDevice);
			}
			
			// response to server
			JsonArray jsonArray = new JsonArray();
			for(String result : responseList) {
				jsonArray.add(result);
			}
			jsonObj = new JsonObject();
			jsonObj.add("result", jsonArray);
			String resultJsonStr = gson.toJson(jsonObj);			
			
			res.setStatus(200);
			res.getWriter().write(resultJsonStr);			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// POST req의 body
	private String getJsonBodyFromRequest(HttpServletRequest req) throws Exception {
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
			throw e;
		}
		return strBody;
	}	
}
