package jscgi.demo;

import java.io.IOException;

import jscgi.server.SCGIServer;

public class HelloWorldServer {

	private static final String RESPONSE_HEADER = "HTTP/1.1 200 OK\n";
	private static final String CONTENT_TYPE = "Content-Type: %s\n";
	private static final String CONTENT_LENGTH = "Content-Length: %d\n";
	private static final String FINISH_HEADER = "\n";

	public static void main(String[] args) {
		try {
			new SCGIServer(65000, (req, res, mode) -> {
				try {
					String message = "Hello, %s!";

					if (req.isBodyAvailable()) {
						message = String.format(message, new String(req.getBody()));
					} else {
						message = String.format(message, "World");
					}
					
					res.write(RESPONSE_HEADER.getBytes());
					res.write(String.format(CONTENT_TYPE, "text/plain").getBytes());
					res.write(String.format(CONTENT_LENGTH, message.getBytes().length + "").getBytes());
					res.write(FINISH_HEADER.getBytes());
					res.write(message.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
