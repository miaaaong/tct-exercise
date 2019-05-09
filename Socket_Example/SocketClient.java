package com.lgcns.socket.server;

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
			while(!input.equals("quit")) {
				outputStream.write(input.getBytes());
				int count = inputStream.read(buffer);
				String received = new String(buffer, 0, count);
				System.out.println(received);
				if(received.equals("ignored")) {
					System.out.println(input + " is ignored");
				}
				
				input = scanner.nextLine();
			}
			outputStream.write(input.getBytes());
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
