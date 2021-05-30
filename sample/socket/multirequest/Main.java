package sample.socket.multirequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static void main(String[] args) {
		// 모든 thread에 공유되어야 하는 초기 정보는 미리 준비하여 static 변수로 설정
		// 예시
		// List<Station> stationList = initStationList();
		// SocketThread.stationList = stationList;
		
		try {
			// socket server 시작
			ServerSocket serverSocket = new ServerSocket(9876);
			
			int threadNum = 100;
			ExecutorService service = Executors.newFixedThreadPool(threadNum);
			for(int i=0; i<threadNum; i++) {
				// 여러 socket client에 대한 요청 처리 준비
				SocketThread thread = new SocketThread(serverSocket);
				service.execute(thread);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
