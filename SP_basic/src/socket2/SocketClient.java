package socket2;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		try (Socket socket = new Socket("127.0.0.1", 9090);
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		FileInputStream fis = new FileInputStream("./data/INPUT/test.exe");) {
			int readLength = 0;
			byte[] buffer = new byte[1024];
			while((readLength = fis.read(buffer)) != -1) {
				os.write(buffer, 0, readLength);
			}
		}
	}
}
