package jscgi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jscgi.Mode;
import jscgi.SCGIMessage;

public class SCGIClientHandler implements Runnable {

	private SCGIRequestHandler requestHandler;

	private Socket socket;

	private InputStream socketIn;
	private OutputStream socketOut;
	
	private Mode mode;

	public SCGIClientHandler(Socket socket, SCGIRequestHandler requestHandler, Mode mode) throws IOException {
		this.mode = mode;
		this.socket = socket;

		socketIn = socket.getInputStream();
		socketOut = socket.getOutputStream();

		this.requestHandler = requestHandler;
	}

	@Override
	public void run() {
		boolean runInLoop = (mode == Mode.SCGI_MESSAGE_BASED);
		try {
			do {
				SCGIMessage request = new SCGIMessage(socketIn);
				requestHandler.handle(request, socketOut, mode);
			} while (runInLoop);
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
