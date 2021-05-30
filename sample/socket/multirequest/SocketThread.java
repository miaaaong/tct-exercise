package sample.socket.multirequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketThread implements Runnable {
	
	// 모든 thread에 공유되어야 하는 정보들은 static
//	private static String targetTime;
//	private static List<BusInfo> busList = new ArrayList<>();
//	private static Map<String, BusInfo> busMap;
//	public static List<Station> stationList;
	
	// 각 thread별 속성
//	private boolean isBusThread = false;
//	private String busId;
//	private String prevInfo;
//	private String lastInfo;
//	private BusInfo busInfo;
	
	private ServerSocket serverSocket;
	
	public SocketThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		Socket socket = null;
		try {
			// client connection 대기
			socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			int count = 0;
			byte[] buffer = new byte[2048];
			while((count = inputStream.read(buffer)) > 0) {
				String value = new String(buffer, 0, count).trim();
				// 수신된 값에 따라 작업
				if(value.equals("test")) {
					//응답해야 하는 경우
					outputStream.write("ignored".getBytes());
				} else if(value.equals("quit")) {
					break;
				} else {
					outputStream.write("received".getBytes());
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
