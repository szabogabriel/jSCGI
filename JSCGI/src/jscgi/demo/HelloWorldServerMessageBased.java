package jscgi.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jscgi.Mode;
import jscgi.SCGIMessage;
import jscgi.server.SCGIServer;

public class HelloWorldServerMessageBased {

	public static void main(String[] args) {
		try {
			new SCGIServer(65001, (req, res, mode) -> {
				try {
					String message = "Hello, %s!";

					if (req.isBodyAvailable()) {
						message = String.format(message, new String(req.getBody()));
					} else {
						message = String.format(message, "World");
					}
					
					Map<String, String> headers = new HashMap<>();
					headers.put("Content-Type", "text/plain");
					headers.put("Content-Length", message.getBytes().length + "");
					
					SCGIMessage ret = new SCGIMessage(headers, message.getBytes());
					
					ret.serialize(res);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}, Mode.SCGI_MESSAGE_BASED);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
