package sample.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class HttpClientPostSample {

	private static String LINE_FEED = "\r\n";
	private static String CHAR_SET = "utf-8";
	
	public static void main(String[] args) {
		String api = "/files";
		File file = new File("./client/cccc.txt");
		
		try {
			URL url = new URL("http://127.0.0.1:3000" + api);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			BigInteger random = new BigInteger(256, new Random());
			String boundary = random.toString();
			connection.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary);
			connection.setRequestMethod("POST");			
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(15000);
			
			// body에 보낼 떄 사용
			OutputStream requestBody = connection.getOutputStream();
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(requestBody, CHAR_SET), true);
			// body에 넣을 데이터가 있는 경우
			writer.append("--" + boundary).append(LINE_FEED);
			writer.append("Content-Disposition: form-data; name=\"key\"").append(LINE_FEED);
			writer.append("Content-Type: text/plain; charset=" + CHAR_SET).append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.append("value").append(LINE_FEED);
			writer.flush();

			/** 파일 데이터를 넣는 부분**/
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append(LINE_FEED);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();
            
            // body에 파일 내용 포함
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                requestBody.write(buffer, 0, bytesRead);
            }
            requestBody.flush();
            inputStream.close();
            writer.append(LINE_FEED);
            writer.flush();
            
            // body 끝
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                System.out.println("success");
                System.out.println(response.toString());
                
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                System.out.println("fail");
                System.out.println(response.toString());
            }

//			https://velog.io/@higod1000/Java-%EC%99%B8%EB%B6%80-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-%EC%97%86%EC%9D%B4-Multipart-Formdata-%EC%A0%84%EC%86%A1%ED%95%98%EA%B8%B0
//			https://kwon8999.tistory.com/entry/HttpURLConnection-Multipart-%ED%8C%8C%EC%9D%BC-%EC%97%85%EB%A1%9C%EB%93%9C

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
