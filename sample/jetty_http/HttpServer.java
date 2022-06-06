package sample.jetty_http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpServer {

	public static void main(String[] args) {
		Server server = new Server();
		ServerConnector httpConnector = new ServerConnector(server);
		httpConnector.setHost("127.0.0.1");
		httpConnector.setPort(8080);
		server.addConnector(httpConnector);
		
		String param1 = "aaaa";
		String param2 = "bbbb";
		
		ServletHandler servletHandler = new ServletHandler();
		ServletHolder servletHolder = servletHandler.addServletWithMapping(SampleServlet.class, "/*");
		servletHolder.setInitParameter("param1", param1);
		servletHolder.setInitParameter("param2", param2);
		
		server.setHandler(servletHandler);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
