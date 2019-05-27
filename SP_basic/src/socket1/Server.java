package socket1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

	public static void main(String[] args) throws IOException {
		ServerSocket listener = new ServerSocket(9090);
		try {
			Socket socket = listener.accept();
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				out.println(format.format(date));
			} finally {
				socket.close();
			}
		} finally {
			listener.close();
		}
	}

}
