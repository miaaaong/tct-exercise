package sample.jetty_http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.eclipse.jetty.server.Request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SampleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String FILE_DIR;
	private static MultipartConfigElement MULTI_PART_CONFIG;
	
	@Override
	public void init() throws ServletException {
		// 초기에 한 번 필요한 작업
		
		// init parameter
		String param1 = getInitParameter("param1");
		String param2 = getInitParameter("param2");
		System.out.println(param1);
		System.out.println(param2);
		
		try {
			File tempUploadDir = new File("./uploads");
			FILE_DIR = tempUploadDir.getCanonicalPath();
			MULTI_PART_CONFIG = new MultipartConfigElement(FILE_DIR);
			// multi part 설정을 다 하는 경우...
			// new MultipartConfigElement(tempDir, multipartRequestMaxSize, multipartRequestMaxSize, multipartReadBufferSize));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// GET 처리
	protected void doGet(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		
		if(requestURI.startsWith("/RECEIVE")) {
			handleGetMethod(req, res);
		} 
	}
	
	// POST 처리
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		
		if(requestURI.startsWith("/CREATE")) {
			handlePostMethod(req, res);
		} else if(requestURI.startsWith("/UPLOAD")) {
			handleFileUpload(req, res);
		}
	}
	
	// GET method
	private void handleGetMethod(HttpServletRequest req, HttpServletResponse res) {
		try {
			String requestURI = req.getRequestURI();
			String[] split = requestURI.split("/");
			String queueName = split[2];
			
			Gson gson = new Gson();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("Result", "Ok");
			jsonObject.addProperty("Queue", queueName);
			jsonObject.addProperty("MessageID", "id");
			jsonObject.addProperty("Message", "value");				
			
			String jsonStr = gson.toJson(jsonObject);
			res.setStatus(200);
			res.getWriter().write(jsonStr);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// POST method
	private void handlePostMethod(HttpServletRequest req, HttpServletResponse res) {
		try {
			String requestURI = req.getRequestURI();
			String[] split = requestURI.split("/");
			String queueName = split[2];

			// body
			String strBody = getJsonBodyFromRequest(req);
			
			Gson gson = new Gson();
			JsonObject jsonObj = gson.fromJson(strBody, JsonObject.class);
			String value = jsonObj.get("Message").getAsString();
			
			//return result
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("Result", value);
			jsonObject.addProperty("Queue", queueName);
			String jsonStr = gson.toJson(jsonObject);
			res.setStatus(200);
			res.getWriter().write(jsonStr);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// POST, multipart/form-data
	private void handleFileUpload(HttpServletRequest req, HttpServletResponse res) {
		try {
			req.setCharacterEncoding("UTF-8");
			res.setContentType("text/html; charset=UTF-8");
			
			Gson gson = new Gson();
			JsonObject jsonObject = new JsonObject();

			String contentType = req.getContentType();
			if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
				req.setAttribute(Request.MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);
				Collection<Part> parts = req.getParts();
				for(Part part : parts) {
					// 파라미터 이름
					System.out.println(part.getName());
					System.out.println(part.getContentType());
					// 파일인 경우 파일 크기
					System.out.println(part.getSize());
					if(part.getHeader("Content-Disposition").contains("filename=")) {
						if(part.getSize() > 0) {
							// 업로드한 파일의 이름
							String fileName = part.getSubmittedFileName();
							part.write(fileName);
							// part와 관련된 파일 삭제
//							part.delete();
							
							jsonObject.addProperty(part.getName(), fileName);
						}
					} else {
						String formValue = req.getParameter(part.getName());
						System.out.printf("name : %s, value : %s \n", part.getName(), formValue);
						jsonObject.addProperty(part.getName(), formValue);
					}
				}
			}
			
			jsonObject.addProperty("Result", "Ok");
			
			String jsonStr = gson.toJson(jsonObject);
			res.setStatus(200);
			res.getWriter().write(jsonStr);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
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
