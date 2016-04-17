package com.jscgi;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JSCGIServer implements Runnable {
	
	private final int PORT;
	private final ExecutorService THREAD_POOL;
	private final ServerSocket SERVER_SOCKET;
	private final JSCGIRequestHandlerFactory REQUEST_HANDLER_FACTORY;
	
	public JSCGIServer(int port, JSCGIRequestHandlerFactory handlerFactory) throws IOException {
		this.PORT = port;
		this.THREAD_POOL = Executors.newCachedThreadPool();
		this.SERVER_SOCKET = new ServerSocket(this.PORT);
		this.REQUEST_HANDLER_FACTORY = handlerFactory;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				this.THREAD_POOL.execute(new JSCGISocketHandler(this.SERVER_SOCKET.accept(), REQUEST_HANDLER_FACTORY.createHandler()));
			} catch (Exception e) {
			}
		}
	}

}
