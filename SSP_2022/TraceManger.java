package com.lgcns.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class TraceManger implements Runnable {

	@Override
	public void run() {
		Server traceServer = new Server();
		ServerConnector traceHttpConnector = new ServerConnector(traceServer);
		traceHttpConnector.setHost("127.0.0.1");
		traceHttpConnector.setPort(9999);
		traceServer.addConnector(traceHttpConnector);
		
		ServletHandler traceServletHandler = new ServletHandler();
		traceServletHandler.addServletWithMapping(TraceServlet.class, "/*");
		
		traceServer.setHandler(traceServletHandler);
		
		try {
			traceServer.start();
			traceServer.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
