package jscgi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jscgi.SCGIMessage;

public class SCGIClientHandler implements Runnable {

	private SCGIRequestHandler requestHandler;

	private Socket socket;

	private InputStream socketIn;
	private OutputStream socketOut;

	public SCGIClientHandler(Socket socket, SCGIRequestHandler requestHandler) throws IOException {
		this.socket = socket;

		socketIn = socket.getInputStream();
		socketOut = socket.getOutputStream();

		this.requestHandler = requestHandler;
	}

	@Override
	public void run() {
		try {
			SCGIMessage request = new SCGIMessage(socketIn);
			requestHandler.handle(request, socketOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
