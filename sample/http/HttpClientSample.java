package sample.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpClientSample {

	public static String ADDRESS = "https://www.google.com";
	public static String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) {
		String api = "";
		sendGet(api);
	
		api = "/aaa";
		String data = "test data";
//		sendPost(api, data);
	}

	public static void sendGet(String api) {
		try {
			URL url = new URL(ADDRESS + api);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			
			connection.setRequestProperty("User-Agent", USER_AGENT);
			
			connection.connect();
			
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendPost(String api, String data) {
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
