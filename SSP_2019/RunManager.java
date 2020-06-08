package com.lgcns.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunManager {

	public static void main(String[] args) {
		socketProcess();
	}
	
	private static void socketProcess() {
		int threadNum = 10;
		ExecutorService service = Executors.newFixedThreadPool(threadNum);
		List<SocketThread> threadList = new ArrayList<>();
		
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(9876);
			for(int i=0; i<threadNum; i++) {
				SocketThread socketThread = new SocketThread(serverSocket, threadList);
				threadList.add(socketThread);
				service.execute(socketThread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
	}
}