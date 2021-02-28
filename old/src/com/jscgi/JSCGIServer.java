package com.jscgi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class JSCGIServer implements Runnable {
	
	private final int PORT;
	private final ServerSocket SERVER_SOCKET;
	private final String HOST;
	private final JSCGIRequestHandlerFactory REQUEST_HANDLER_FACTORY;
	
	public JSCGIServer(int port, JSCGIRequestHandlerFactory handlerFactory) throws IOException {
		this.PORT = port;
		this.HOST = "";
		this.SERVER_SOCKET = new ServerSocket(this.PORT);
		this.REQUEST_HANDLER_FACTORY = handlerFactory;
	}
	
	public JSCGIServer(String host, int port, JSCGIRequestHandlerFactory handlerFactory) throws IOException {
		this.PORT = port;
		this.HOST = host;
		this.SERVER_SOCKET = new ServerSocket(this.PORT, 0, InetAddress.getByName(this.HOST));
		this.REQUEST_HANDLER_FACTORY = handlerFactory;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
//				this.THREAD_POOL.execute(new JSCGISocketHandler(this.SERVER_SOCKET.accept(), REQUEST_HANDLER_FACTORY.createHandler()));
				REQUEST_HANDLER_FACTORY.register(new JSCGISocketHandler(this.SERVER_SOCKET.accept(), REQUEST_HANDLER_FACTORY.createHandler()));
			} catch (Exception e) {
			}
		}
	}

}
