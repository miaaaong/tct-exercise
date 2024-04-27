package com.lgcns.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class RunManager {

	public static void main(String[] args) {
		Server server = new Server();
		ServerConnector httpConnector = new ServerConnector(server);
		httpConnector.setHost("127.0.0.1");
		httpConnector.setPort(8010);
		server.addConnector(httpConnector);
		
		ServletHandler servletHandler = new ServletHandler();
		ServletHolder servletHolder = servletHandler.addServletWithMapping(EdgeNodeServlet.class, "/*");
		
		server.setHandler(servletHandler);
		
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
