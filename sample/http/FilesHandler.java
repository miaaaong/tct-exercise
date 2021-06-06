package sample.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FilesHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String method = exchange.getRequestMethod();

		switch (method) {
		case "PUT":
			receiveFile(exchange);
			break;
		case "POST":
			createFile(exchange);
			break;
		default:
			sendFileList(exchange);
		}
		
		System.out.println("finish service");
	}
	
	private void createFile(HttpExchange exchange) {
		
		try {
			InputStream requestBody = exchange.getRequestBody();
			BufferedReader br = new BufferedReader(new InputStreamReader(requestBody, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = br.readLine()) != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
			}
			br.close();
			
			String value = sb.toString().trim().substring(1, sb.length()-2);
			String fileName = null;
			String contents = null;
			String[] split = value.split(",");
			for(String element : split) {
				String[] split2 = element.split(":");
				if(split2[0].trim().replaceAll("\"", "").equals("fileName")) {
					fileName = split2[1].trim().replaceAll("\"", "");
				} else if(split2[0].trim().replaceAll("\"", "").equals("contents")) {
					contents = split2[1].trim().replaceAll("\"", "");
				}
			}
			
			contents = contents.replaceAll("}", "");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./server/" + fileName)));
			bw.write(contents);
			bw.close();
			
			// send response
			String response = "success";
			exchange.sendResponseHeaders(200, response.length());
			OutputStream responseBody = exchange.getResponseBody();
			responseBody.write(response.getBytes());
			responseBody.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally {

		}
		
	}

	private void receiveFile(HttpExchange exchange) {
		Headers requestHeaders = exchange.getRequestHeaders();
//		Set<Entry<String,List<String>>> entrySet = requestHeaders.entrySet();
//		Iterator<Entry<String, List<String>>> iterator = entrySet.iterator();
//		System.out.println("headers");
//		System.out.println("================================");
//		while(iterator.hasNext()) {
//			Entry<String, List<String>> next = iterator.next();
//			System.out.println(next.getKey());
//			System.out.println(next.getValue());
//		}
//		System.out.println("================================");
		
		try {
			String[] contentTypeValues = requestHeaders.get("Content-Type").get(0).split(";");
			if(!"multipart/form-data".equals(contentTypeValues[0])) {
				String message = "content type is wrong";
//			Headers headers = exchange.getResponseHeaders();
//			headers.add("Content-Type", "text/html;charset=UTF-8");
//			headers.add("Content-Length", String.valueOf(message.length()));
				// 종료
				exchange.sendResponseHeaders(200, message.length());
				OutputStream responseBody = exchange.getResponseBody();
				responseBody.write(message.getBytes());
				responseBody.close();
				return;
			}
			
			String charset = "utf-8";
			String boundary = "";
			for(int i=1; i<contentTypeValues.length; i++) {
				String[] split = contentTypeValues[i].split("=");
				if("charset".equals(split[0].trim())) {
					charset = split[1].trim();
				} else if("boundary".equals(split[0].trim())) {
					boundary = split[1].trim();
				}
			}
			
			InputStream requestBody = exchange.getRequestBody();
			BufferedReader br = new BufferedReader(new InputStreamReader(requestBody, charset));
			
			boolean nextIsValue = false;
			String fileName = null;
			StringBuilder sb = new StringBuilder();

			String line = null;
			while((line = br.readLine()) != null) {
				System.out.println(line);
				if(line.equals("--" + boundary + "--")) {
					//종료
					break;
				} else if(!line.trim().equals("--" + boundary)) {
					if(line.equals("")) {
						nextIsValue = true;
					} else if(line.indexOf("filename") >= 0) {
						String[] split = line.split(";");
						for(String element : split) {
							if(element.trim().indexOf("filename") >= 0) {
								fileName = element.trim().split("=")[1].trim().replaceAll("\"", "");
								break;
							}
						}
					} else if(nextIsValue) {
						sb.append(line);
						sb.append(System.lineSeparator());
					}
				} else {
					// 초기화
					nextIsValue = false;
				}
			}
			
			br.close();
			
			if(fileName == null) {
				String message = "file name is not provided";
				// 종료
				exchange.sendResponseHeaders(200, message.length());
				OutputStream responseBody = exchange.getResponseBody();
				responseBody.write(message.getBytes());
				responseBody.close();
				return;
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./server/" + fileName)));
			bw.write(sb.toString());
			bw.close();
			
			// send response
			String response = "file is successfully saved";
			exchange.sendResponseHeaders(200, response.length());
			OutputStream responseBody = exchange.getResponseBody();
			responseBody.write(response.getBytes());
			responseBody.close();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendFileList(HttpExchange exchange) {
		OutputStream respBody = exchange.getResponseBody();
		Headers headers = exchange.getResponseHeaders();

		File dir = new File("./server");
		String[] list = dir.list();
		StringBuilder sb = new StringBuilder();
		sb.append("{\"files\": [");
		for (int i = 0; i < list.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			String name = list[i];
			sb.append("\"");
			sb.append(name);
			sb.append("\"");
		}
		sb.append("]}");

//		Content-Type: text/html; charset=utf-8
//		Content-Type: application/json
//		Content-Type: multipart/form-data; boundary=something
		headers.add("Content-Type", "application/json;charset=UTF-8");
		headers.add("Content-Length", String.valueOf(sb.toString().length()));

		try {
			// Send Response Headers
			exchange.sendResponseHeaders(200, sb.toString().length());

			respBody.write(sb.toString().getBytes());

			// Close Stream
			// 반드시, Response Header를 보낸 후에 닫아야함
			respBody.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (respBody != null) {
				try {
					respBody.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
