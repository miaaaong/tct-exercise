package sample.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientPost {

	public static void main(String[] args) {
		String api = "/files";
		
		String fileName = "dddd.txt";
		StringBuilder sb = new StringBuilder();
		sb.append("ddddd");
		sb.append(System.lineSeparator());
		sb.append("asdfafadf");
		sb.append(System.lineSeparator());
		sb.append("sdlkjflkdjfkdf");
		sb.append(System.lineSeparator());
		sb.append("wer;kjdlkfjslkdjfkdf");
		
		String body = "{\"fileName\": \"" + fileName + "\", \"contents\": \"" + sb.toString() + "\"}";
		
		HttpClient httpClient = HttpClient.newHttpClient();
		
		try {
			HttpRequest httpRequest = HttpRequest.newBuilder()
			.uri(URI.create("http://127.0.0.1:5000" + api))
			.setHeader("Content-Type", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(body))
			.build();
			
			HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
			System.out.println("response code : " + response.statusCode());
			System.out.println("response body : " + response.body());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
