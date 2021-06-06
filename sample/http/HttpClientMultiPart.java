package sample.http;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HttpClientMultiPart {
	
	private static final String LINEFEED = "\r\n";
	private static final String DOUBLE_HYPHEN = "--";
	private static final String QUOTATION = "\"";

	public static void main(String[] args) {
		String api = "/files";
		
		Path filePath = Path.of("./client/cccc.txt");
		
		BigInteger random = new BigInteger(256, new Random());
		String boundary = random.toString();
		
		Map<String, Object> map = new HashMap<>();	// 또는 object, object
		map.put("file", filePath);
		
		HttpClient httpClient = HttpClient.newHttpClient();
		
		try {
			BodyPublisher bodyPublisher = multipartToByte(map, boundary);
			
			HttpRequest httpRequest = HttpRequest.newBuilder()
			.uri(URI.create("http://127.0.0.1:5000" + api))
			.setHeader("Accept-Language", "ko")
			.setHeader("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary)
			.PUT(bodyPublisher)
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

	private static HttpRequest.BodyPublisher multipartToByte(Map<String, Object> map, String boundary) throws IOException {
		List<byte[]> byteArrays = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();

		for (Map.Entry<String, Object> data : map.entrySet()) {
			stringBuilder.setLength(0);
			// 항목 시작
			stringBuilder.append(DOUBLE_HYPHEN).append(boundary).append(LINEFEED);
			
			if (data.getValue() instanceof Path) {
				// 파일인 경우
				Path filePath = (Path) data.getValue();
				String fileName = filePath.getFileName().toString();
				String mimeType = Files.probeContentType(filePath);
				byte[] fileByte = Files.readAllBytes(filePath);

				stringBuilder.append("Content-Disposition: form-data; name=").append(QUOTATION).append(data.getKey()).append(QUOTATION)
					.append("; filename= ").append(QUOTATION).append(fileName).append(QUOTATION)
					.append(LINEFEED)
					.append("Content-Type: ").append(mimeType)
					.append(LINEFEED)
					.append(LINEFEED);	// value(file 내용)는 2줄 띄우고 보냄

				byteArrays.add(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
				byteArrays.add(fileByte);
				// value 후 한 줄 더 띄움
				byteArrays.add(LINEFEED.getBytes(StandardCharsets.UTF_8));
			} else {
				// 그 외 값 (key-value)
				stringBuilder.append("Content-Disposition: form-data; name=").append(QUOTATION).append(data.getKey()).append(QUOTATION).append(";")
					.append(LINEFEED)
					.append(LINEFEED)
					.append(data.getValue()) // value는 2줄 띄우고 보냄
					.append(LINEFEED);
				// value 후 한 줄 더 띄움				
				byteArrays.add(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
			}
		}

		// 마지막에 --boundary--
		stringBuilder.setLength(0);
		stringBuilder.append(DOUBLE_HYPHEN).append(boundary).append(DOUBLE_HYPHEN);
		byteArrays.add(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));

		return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
	}
}
