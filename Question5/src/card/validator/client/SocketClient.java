package card.validator.client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {

	public static void sendInspectionFiles(String empId) throws IOException {
		File[] inspectionFiles = FileManager.getInspectionFiles(empId);
		try (Socket socket = new Socket("127.0.0.1", 9090);
			DataOutputStream d = new DataOutputStream(socket.getOutputStream())) {
			for(File file : inspectionFiles) {
				String fileName = file.getName();
				d.writeUTF(fileName);
				d.writeLong(file.length());
				d.flush();
				byte[] buffer = new byte[1024];
				try(FileInputStream fis = new FileInputStream(file)) {
					int read = 0;
					while((read = fis.read(buffer)) != -1) {
						d.write(buffer, 0, read);
					}
					d.flush();
				}
			}
		} 
	}
}
