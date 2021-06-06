package sample.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GatewayHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println(exchange.getRequestURI());
		System.out.println(exchange.getHttpContext().getPath());
		
		String api = "/files";

//		Builder builder = HttpRequest.newBuilder(URI.create("http://127.0.0.1:3000" + api));

		// body
		String strBody = getRequestBody(exchange.getRequestBody());
		System.out.println(strBody);
		
		// headers
//		Headers requestHeaders = exchange.getRequestHeaders();
//		Set<Entry<String,List<String>>> entrySet = requestHeaders.entrySet();
//		Iterator<Entry<String, List<String>>> iterator = entrySet.iterator();
//		while(iterator.hasNext()) {
//			Entry<String, List<String>> next = iterator.next();
//			String key = next.getKey();
//			List<String> value = next.getValue();
//			System.out.println(value.size());
//			builder.setHeader(key, value.get(0));
//			System.out.println(key);
//			System.out.println(value.get(0));
//		}

		URL url = new URL("http://127.0.0.1:3000" + api);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");			
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setRequestProperty("Content-Type", "application/json");
		
		OutputStream requestBody = connection.getOutputStream();
		requestBody.write(strBody.getBytes());
		requestBody.flush();
		requestBody.close();
		
		int responseCode = connection.getResponseCode();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuffer response = new StringBuffer();
		String inputLine;
		while ((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		br.close(); 
		
		System.out.println("HTTP 응답 코드 : " + responseCode);
		System.out.println("HTTP body : " + response.toString());
		
		exchange.sendResponseHeaders(responseCode, response.length());
		OutputStream responseBody = exchange.getResponseBody();
		responseBody.write(response.toString().getBytes());
		responseBody.close();

//		String requestMethod = exchange.getRequestMethod();
//		if(requestMethod.equals("POST")) {
//			builder.POST(BodyPublishers.ofString(strBody));
//		} else if(requestMethod.equals("PUT")) {
//			builder.PUT(BodyPublishers.ofString(strBody));
//		}

//		HttpRequest httpRequest = builder.build();
		
//		HttpClient httpClient = HttpClient.newHttpClient();
//		HttpResponse<String> response;
//		try {
//			response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//			System.out.println("response code : " + response.statusCode());
//			System.out.println("response body : " + response.body());
//			
//			exchange.sendResponseHeaders(response.statusCode(), response.body().length());
//			OutputStream responseBody = exchange.getResponseBody();
//			responseBody.write(response.body().getBytes());
//			responseBody.close();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		System.out.println("finish gateway");
	}
	
	private String getRequestBody(InputStream requestBody) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(requestBody, "utf-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while((line = br.readLine()) != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
		}
		br.close();
		
		return sb.toString();
	}

}
