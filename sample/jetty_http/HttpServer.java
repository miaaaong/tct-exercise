package sample.jetty_http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class HttpServer {

	public static void main(String[] args) {
		Server server = new Server();
		ServerConnector httpConnector = new ServerConnector(server);
		httpConnector.setHost("127.0.0.1");
		httpConnector.setPort(8080);
		server.addConnector(httpConnector);
		
		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(SampleServlet.class, "/*");
		server.setHandler(servletHandler);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
