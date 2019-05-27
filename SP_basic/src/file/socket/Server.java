package file.socket;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {

	public static void main(String[] args) throws IOException {
		ServerSocket listener = new ServerSocket(9090);
		try {
			Socket socket = listener.accept();
			try {
				BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
				try (DataInputStream d = new DataInputStream(in)) {
				    String fileName = d.readUTF();
				    Files.copy(d, Paths.get("./test/" + fileName));
				}
			} finally {
				socket.close();
			}
		} finally {
			listener.close();
		}
	}

}
