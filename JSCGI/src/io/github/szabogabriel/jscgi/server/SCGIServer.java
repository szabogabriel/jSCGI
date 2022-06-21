package io.github.szabogabriel.jscgi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.szabogabriel.jscgi.Mode;

/**
 * An SCGI server implementation. It creates a {@link java.net.ServerSocket}.
 * The Server Socket will listen on a specific port, however, it will use all
 * the available network interfaces.
 * 
 * @author gszabo
 *
 */
public class SCGIServer {

	private ServerSocket ss;

	private SCGIRequestHandler requestHandler;

	private ExecutorService executor = Executors.newCachedThreadPool();

	private Thread t;

	private Mode mode;

	/**
	 * Create a SCGIServer instance.
	 * 
	 * @param port           port on which the server should listen.
	 * @param requestHandler the request handler to be used for incoming requests.
	 * @throws IOException
	 */
	public SCGIServer(int port, SCGIRequestHandler requestHandler) throws IOException {
		this(port, requestHandler, Mode.STANDARD);
	}

	/**
	 * Create an SCGIServer instance.
	 * 
	 * @param port           port on which the server should listen.
	 * @param requestHandler the request handler to be used for incoming requests.
	 * @param mode           the SCGI application mode to be used.
	 * @throws IOException
	 */
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

	/**
	 * This method will interrupt the currently running thread effectively stopping
	 * the Server Socket from listening.
	 */
	public void stop() {
		t.interrupt();
	}

}
