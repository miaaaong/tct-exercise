package sample.jetty_http;

import java.io.File;
import java.nio.file.Paths;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.MultiPartContentProvider;
import org.eclipse.jetty.client.util.PathContentProvider;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

public class SampleClient {

	public static void main(String[] args) {
		HttpClient httpClient = new HttpClient();

		// 1. GET
		try {
			httpClient.start();
			// 참고 - url에 whitespace 등 특수문자가 포함되는 경우 변환할 것
			// URLEncoder.encode(value, "UTF-8")
			ContentResponse contentResponse = httpClient.newRequest("http://127.0.0.1:8080/RECEIVE/test")
					.method(HttpMethod.GET).send();
			System.out.println(contentResponse.getContentAsString());
			httpClient.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 2. POST
		try {
			String jsonParam = "{\"Message\": \"message!!\"}";

			httpClient.start();
			Request request = httpClient.newRequest("http://127.0.0.1:8080/CREATE/test").method(HttpMethod.POST);
			request.header(HttpHeader.CONTENT_TYPE, "application/json");
			request.content(new StringContentProvider(jsonParam, "utf-8"));
			ContentResponse contentResponse = request.send();
			System.out.println(contentResponse.getContentAsString());
			httpClient.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 3. multipart/form-data
		try {
			File file = new File("./sample.txt");
			
			MultiPartContentProvider multiPart = new MultiPartContentProvider();
			multiPart.addFieldPart("field", new StringContentProvider("foo"), null);
			multiPart.addFilePart("uploadfile", "sample.txt", new PathContentProvider(Paths.get(file.getCanonicalPath())), null);
			multiPart.close();
			
			httpClient.start();
			ContentResponse contentResponse = httpClient.newRequest("http://127.0.0.1:8080/UPLOAD").method(HttpMethod.POST)
					.content(multiPart).send();
			System.out.println(contentResponse.getContentAsString());
			httpClient.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
