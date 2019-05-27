package socket2;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws InterruptedException, UnknownHostException, IOException {
		SocketServer socketServer = new SocketServer();
		Thread thread = new Thread(socketServer);
		thread.start();
		
		Scanner scan = new Scanner(System.in);
		String message = "";
		while (!message.equals("QUIT")) {
			System.out.println("enter message :");
			message = scan.nextLine();
			System.out.println("entered message : " + message);
			if(message.equals("QUIT")) {
				socketServer.stopListener();
			}
		}
		
		thread.join();
	}

}
