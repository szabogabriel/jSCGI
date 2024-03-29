package io.github.szabogabriel.jscgi.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

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
public class AsyncSCGIClientHandler {
		
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
	public AsyncSCGIClientHandler(Socket socket, SCGIRequestHandler requestHandler, Mode mode) throws IOException {
		this.mode = mode;
		this.socket = socket;

		socketIn = socket.getInputStream();
		socketOut = socket.getOutputStream();

		this.requestHandler = requestHandler;
	}

	public void run() {
		CompletableFuture.runAsync(() -> {
			boolean runInLoop = (mode == Mode.SCGI_MESSAGE_BASED);
			do {
				CompletableFuture.supplyAsync(this::acceptScgiMessage)
					.thenAcceptAsync(this::handleScgiMessage)
					.thenRun(this::cleanup);
			} while (runInLoop);
		});
	}
	
	private SCGIMessage acceptScgiMessage() {
		try {
			return new SCGIMessage(socketIn);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return new SCGIMessage();
	}
	
	private void handleScgiMessage(SCGIMessage request) {
		requestHandler.handle(request, socketOut, mode);
	}
	
	private void cleanup() {
		try {
			if (Mode.STANDARD.equals(mode)) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
