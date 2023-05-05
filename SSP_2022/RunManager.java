package com.lgcns.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RunManager {

	public static void main(String[] args) {
		TraceManger traceManager = new TraceManger();
		Thread t = new Thread(traceManager);
		t.start();
		
		String routingRuleFilePath = args[0];
		String routingRuleConfiguration = getRoutingRuleConfiguration(routingRuleFilePath);
		
		JsonElement jsonElement = JsonParser.parseString(routingRuleConfiguration);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		JsonElement portElement = jsonObject.get("port");
		int port = portElement.getAsInt();
		System.out.println("port => " + port);
		JsonElement routesElement = jsonObject.get("routes");
		
		Server server = new Server();
		ServerConnector httpConnector = new ServerConnector(server);
		httpConnector.setHost("127.0.0.1");
		httpConnector.setPort(port);
		server.addConnector(httpConnector);
		
		ServletHandler servletHandler = new ServletHandler();
		ServletHolder servletHolder = servletHandler.addServletWithMapping(RouteServlet.class, "/*");
		servletHolder.setInitParameter("routes", routesElement.toString());
		servletHolder.setInitParameter("port", Integer.toString(port));
		
		server.setHandler(servletHandler);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static String getRoutingRuleConfiguration(String path) {
		File configFile = new File(path);
		StringBuilder sb = new StringBuilder();
		try (FileReader fr = new FileReader(configFile); 
				BufferedReader br = new BufferedReader(fr);) {
			String line = "";
			while ((line = br.readLine()) != null) {
				if(sb.length() != 0) {
					sb.append("\n");
				}
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
}
