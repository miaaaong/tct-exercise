package com.lgcns.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ProxyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Map<String,String> routes = new HashMap<String, String>();
	
	@Override
	public void init() throws ServletException {
		// init parameter
		String routesConfig = getInitParameter("routes");
		
		JsonElement routesElement = JsonParser.parseString(routesConfig);
		JsonArray routesArray = routesElement.getAsJsonArray();
		for(int i=0; i<routesArray.size(); i++) {
			JsonObject route = routesArray.get(i).getAsJsonObject();
			routes.put(route.get("pathPrefix").getAsString(), route.get("url").getAsString());
		}
	}
	
	// GET 贸府
	protected void doGet(HttpServletRequest req, HttpServletResponse res) {
		handleProxy(req, res, HttpMethod.GET);
	}
	
	// POST 贸府
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		handleProxy(req, res, HttpMethod.POST);
	}
	
	private void handleProxy(HttpServletRequest req, HttpServletResponse res, HttpMethod method) {
		String requestURI = req.getRequestURI();
		System.out.println(method.asString() + " original request => " + requestURI);
		
		// target URI
		String[] split = requestURI.split("/");
		String firstPath = "/" + split[1];
		String targetUrl = routes.get(firstPath);
		String targetRequestURI = targetUrl + requestURI;
		
		// parameters of request
		String parameters = getParameters(req);
		targetRequestURI += parameters;
		
		System.out.println(method.asString() + " target request => " + targetRequestURI);
		
		try {
			HttpClient httpClient = new HttpClient();
			httpClient.start();
			
			Request request = httpClient.newRequest(targetRequestURI).method(method);
			
			// headers of request
			copyHeaders(req, request);
			
			// body from request
			String body = getBody(req);
			request.content(new StringContentProvider(body));
			
			// call
			ContentResponse contentResponse = request.send();
			
			// return
			System.out.println(contentResponse.getContentAsString());
			httpClient.stop();

			// status of response
			res.setStatus(contentResponse.getStatus());
			
			// headers of response
			copyHeaders(contentResponse, res);
			
			// return with value (body)
			res.getWriter().write(contentResponse.getContentAsString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// parameters from request
	private String getParameters(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> parameterNames = req.getParameterNames();
		while(parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			if(sb.length() == 0) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(paramName);
			sb.append("=");
			sb.append(req.getParameter(paramName));
		}
		return sb.toString();
	}
	
	// body from request 
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
	
	// request to request
	private void copyHeaders(HttpServletRequest originRequest, Request targetRequest) {
		Enumeration<String> headerNames = originRequest.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			targetRequest.header(headerName, originRequest.getHeader(headerName));
		}
	}
	
	// response to response
	private void copyHeaders(ContentResponse originResponse, HttpServletResponse targetResponse) {
		HttpFields httpFields = originResponse.getHeaders();
		Iterator<HttpField> iterator = httpFields.iterator();
		while(iterator.hasNext()) {
			HttpField httpField = iterator.next();
			targetResponse.setHeader(httpField.getName(), httpField.getValue());
		}
	}

}
