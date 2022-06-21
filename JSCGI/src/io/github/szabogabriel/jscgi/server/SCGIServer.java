package io.github.szabogabriel.jscgi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.szabogabriel.jscgi.Mode;

public class SCGIServer {
	
	private ServerSocket ss;
	
	private SCGIRequestHandler requestHandler;
	
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	private Thread t;
	
	private Mode mode;
	
	public SCGIServer(int port, SCGIRequestHandler requestHandler) throws IOException {
		this(port, requestHandler, Mode.STANDARD);
	}
	
	public SCGIServer(int port, SCGIRequestHandler requestHandler, Mode mode) throws IOException {
		this.mode = mode;
		this.requestHandler = requestHandler;
		ss = new ServerSocket(port);
		t = new Thread(this::handleNewConnection);
		t.start();
	}
	
	private void handleNewConnection() {
		while (true) {
			try {
				Socket socket = ss.accept();
				executor.execute(new SCGIClientHandler(socket, requestHandler, mode));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		t.interrupt();
	}
	
}
