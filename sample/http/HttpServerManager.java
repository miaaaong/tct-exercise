package sample.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.sun.net.httpserver.HttpServer;

/**
 * Main Class
 */
public class HttpServerManager {
	
	private static HttpServer server = null;

	public static void main(String[] args) {
		try {
			// 시작 로그
			System.out.println(String.format("[%s][HTTP SERVER][START]",
					new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date())));

			// HTTP Server 생성
			//HttpServer.create(new InetSocketAddress(port), 0);
			server = HttpServer.create(new InetSocketAddress("0.0.0.0", 3000), 0);
			
			// context handlers
			setContexts();
			
			// 기동
			server.start();
			
			// Shutdown Hook
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					// 종료 로그
					System.out.println(String.format("[%s][HTTP SERVER][STOP]",
							new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date())));
				}
			}));

			// Enter를 입력하면 종료
			System.out.println("Please press 'Enter' to stop the server.");
			System.out.println(System.in.read());
			
//			Scanner scanner = new Scanner(System.in);
//			String input = scanner.nextLine();
//			while(!"stop".equals(input)) {
//				input = scanner.nextLine();
//			}
			// 0초 대기후 종료
			server.stop(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(server != null) {
				server.stop(0);
			}
		}
	}
	
	private static void setContexts() {
		// HTTP Server Context 설정
		server.createContext("/", new RootHandler());
		server.createContext("/files", new FilesHandler());		
	}

}