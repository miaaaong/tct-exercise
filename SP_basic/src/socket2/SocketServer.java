package socket2;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable {
	
	private ServerSocket listener = null;
	
	public void stopListener() throws IOException {
		this.listener.close();
	}
	
	@Override
	public void run() {
		listener = null;
		try {
			listener = new ServerSocket(9090);
			Socket socket = listener.accept();
			byte[] bytes = new byte[1024];
			
			try (DataInputStream in = new DataInputStream(socket.getInputStream());
					OutputStream os = new FileOutputStream("./socket/test.exe")) {
				int readLength = 0;
				while((readLength = in.read(bytes)) != -1) {
					os.write(bytes, 0, readLength);
				}
			}
			
			while(true) {
				socket = listener.accept();
			}
		} catch (Throwable e) {
//			e.printStackTrace();
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
//				e.printStackTrace();s
			}
		}
	}

}
