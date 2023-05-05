package com.lgcns.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TraceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private Map<String, ServiceTrace> traceMap = new HashMap<String, ServiceTrace>();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		String[] split = requestURI.split("/");
		String requestId = split[split.length-1];
		ServiceTrace serviceTrace = traceMap.get(requestId);
		Gson gson = new Gson();
		String strJson = gson.toJson(serviceTrace);
		
		res.setStatus(200);
		try {
			res.getWriter().write(strJson);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		String strBody = getBody(req);
		JsonObject jsonObject = JsonParser.parseString(strBody).getAsJsonObject();
		String requestId = jsonObject.get("requestId").getAsString();
		String from = jsonObject.get("from").getAsString();
		String to = jsonObject.get("to").getAsString();
		String status = jsonObject.get("status").getAsString();

		if(traceMap.containsKey(requestId)) {
			ServiceTrace serviceTrace = traceMap.get(requestId);
			ServiceTrace target = serviceTrace.findTarget(from);
			if(target == null) {
				List<ServiceTrace> list = serviceTrace.getServices().get(0).getServices();
				list.add(new ServiceTrace(from, status, to));
			} else {
				target.getServices().add(new ServiceTrace(to, status));
			}
		} else {
			// new
			ServiceTrace serviceTrace = new ServiceTrace(from, status, to);
			traceMap.put(requestId, serviceTrace);
		}
		
		res.setStatus(200);
		try {
			res.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getBody(HttpServletRequest req) {
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

}
