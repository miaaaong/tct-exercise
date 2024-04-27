package com.lgcns.test;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WorkflowServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Map<String, Workflow> workflowMap = new HashMap<String, Workflow>();
	

	@Override
	public void init() throws ServletException {
		String filePath = "./WORKFLOW.JSON";
		JsonObject jsonObject = null;
		try (FileReader fr = new FileReader(filePath);) {
			jsonObject = JsonParser.parseReader(fr).getAsJsonObject();
			jsonObject = jsonObject.get("workflow").getAsJsonObject();
			Set<Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
			Iterator<Entry<String, JsonElement>> iterator = entrySet.iterator();
			while (iterator.hasNext()) {
				Entry<String, JsonElement> next = iterator.next();
				String workflowName = next.getKey();
				JsonObject workflowInfo = next.getValue().getAsJsonObject();
				Workflow workflow = InfoParser.getWorkflow(workflowInfo);
				workflowMap.put(workflowName, workflow);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	// GET Ã³¸®
	protected void doGet(HttpServletRequest req, HttpServletResponse res) {
		handleGetMethod(req, res);
	}

	// GET method
	private void handleGetMethod(HttpServletRequest req, HttpServletResponse res) {
		try {
			String requestURI = req.getRequestURI();
			String[] split = requestURI.split("/");
			String workflowName = split[1];
			
			Workflow workflow = workflowMap.get(workflowName);
			workflow.run();
			
			List<String> responses = workflow.getResponses();
			JsonObject returnObj = new JsonObject();
			for(String key : responses) {
				String value = Variables.getValue(key);
				returnObj.addProperty(key, value);
			}
			Gson gson = new Gson();
			String resultJsonStr = gson.toJson(returnObj);
			res.setStatus(200);
			res.getWriter().write(resultJsonStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
