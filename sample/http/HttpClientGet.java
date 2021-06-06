package sample.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.net.ssl.HttpsURLConnection;

public class HttpClientGet {

	public static String ADDRESS = "http://127.0.0.1:3000";
	public static String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) {
		String api = "/files";
		sendGet(api);
	
		api = "/aaa";
		String data = "test data";
//		sendPut(api, data);
	}

	public static void sendGet(String api) {
		try {
			HttpClient httpClient = HttpClient.newHttpClient();
			
			HttpRequest httpRequest = HttpRequest.newBuilder()
			.uri(URI.create(ADDRESS + api))
			.setHeader("User-Agent", USER_AGENT)
			.GET()
			.build();
			
			HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
			System.out.println("response code : " + response.statusCode());
			System.out.println("response body : " + response.body());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendPut(String api, String data) {
		try {			
			URL url = new URL(ADDRESS + api);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			
			con.setRequestMethod("POST");
			
			con.setRequestProperty("User-Agent", USER_AGENT);
			// /json으로 message를 전달하고자 할 때
			con.setRequestProperty("Accept", "application/json");
			
			// POST 파라미터 전달을 위한 설정
			con.setDoOutput(true);  
			
			con.connect();
			
			// Send post request
//			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//			wr.writeBytes(data);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			bw.write(data);
			bw.flush();
			bw.close();
			
			int responseCode = con.getResponseCode();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close(); 
			
			System.out.println("HTTP 응답 코드 : " + responseCode);
			System.out.println("HTTP body : " + response.toString());
		} catch (Exception e) {
			
		}

	}
}
