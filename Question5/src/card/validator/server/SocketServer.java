package card.validator.server;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable {
	
	private ServerSocket listener;
	
	public void stopListener() {
		try {
			this.listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			this.listener = new ServerSocket(9090);
			while(true) {
				Socket socket = this.listener.accept();
				File dir = new File("../SERVER");
				if(!dir.exists()) {
					dir.mkdirs();
				}
				try (DataInputStream d = new DataInputStream(socket.getInputStream())) {
					while(true) {
						String fileName = d.readUTF();
						long fileLength = d.readLong();
						long remainLength = fileLength;
						int bufferLength = 1024;
						byte[] buffer = new byte[bufferLength];
						
						File file = new File(dir, fileName);
						try(FileOutputStream fos = new FileOutputStream(file)) {
							while(remainLength > 0L) {
								int readLength = bufferLength;
								if(remainLength < bufferLength) {
									//buffer보다 적은 바이트가 남았으면 그것만 읽어야 함
									readLength = (int)remainLength;
								}
								
								int read = d.read(buffer, 0, readLength);
								fos.write(buffer, 0, read);
								remainLength -= read;
							}
						}
					}
				} catch(EOFException t) {
//					t.printStackTrace();
				}
				socket.close();
			}
//		} catch (IOException e) {
//			e.printStackTrace();
		} catch (Throwable t) {
			
		}
	}

}
