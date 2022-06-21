package io.github.szabogabriel.jscgi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import io.github.szabogabriel.jscgi.Mode;
import io.github.szabogabriel.jscgi.SCGIMessage;

/**
 * An SCGI client handler used by the socket. It receives an SCGIRequestHandler,
 * which is then used to pass the SCGIMessage to, which was received from the
 * client.
 * 
 * @author gszabo
 *
 */
public class SCGIClientHandler implements Runnable {

	private SCGIRequestHandler requestHandler;

	private Socket socket;

	private InputStream socketIn;
	private OutputStream socketOut;

	private Mode mode;

	/**
	 * Create a new SCGIClientHandler instance.
	 * 
	 * @param socket         socket for the client connection.
	 * @param requestHandler the request handler to be used with the client.
	 * @param mode           the type of operation mode.
	 * @throws IOException
	 */
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
