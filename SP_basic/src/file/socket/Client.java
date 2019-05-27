package file.socket;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1", 9090);
		
		File file = new File("./data/INPUT/desktop.js");
		String fileName = file.getName();
		BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
		try (DataOutputStream d = new DataOutputStream(out)) {
		    d.writeUTF(fileName);
		    Files.copy(file.toPath(), d);
		}
		
		out.close();
		socket.close();
	}

}
