package com.lgcns.socket.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	public static void main(String[] args) {
		try {
			//socket 생성
			ServerSocket serverSocket = new ServerSocket(9876);
			//client 대기
			Socket socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			int count = 0;
			byte[] buffer = new byte[2048];
			while((count = inputStream.read(buffer)) > 0) {
				String value = new String(buffer, 0, count);
				System.out.println(value);
				if(value.equals("test")) {
					outputStream.write("ignored".getBytes());
				} else if(value.equals("quit")) {
					break;
				} else {
					outputStream.write("received".getBytes());
				}
			}
			
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
