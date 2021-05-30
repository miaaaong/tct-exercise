package sample.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient {

	public static void main(String[] args) {
		Socket socket = null;
		try {
			socket = new Socket("localhost", 9876);
			
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			byte[] buffer = new byte[2048];
			
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			// 전송 로직 구현 예시
			while(!input.equals("quit")) {
				// 서버에 메시지 전송
				outputStream.write(input.getBytes());
				// 통신 완료 조건 있는 경우 붙여서 전송
				outputStream.write("\n".getBytes());

				outputStream.flush();

				// 서버의 응답 수신
				int count = inputStream.read(buffer);
				String received = new String(buffer, 0, count);
				System.out.println(received);
				// 응답 메시지에 따른 로직 예시
				if(received.equals("ignored")) {
					System.out.println(input + " is ignored");
				}
				
				input = scanner.nextLine();
			}
			outputStream.write(input.getBytes());
			outputStream.write("\n".getBytes());
			outputStream.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
