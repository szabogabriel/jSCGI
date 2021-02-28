package jscgi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import jscgi.client.SCGIClient;
import jscgi.server.SCGIRequestHandler;
import jscgi.server.SCGIServer;

public class MainFlowTest {
	
	int port = 65000;
	
	@Test
	void testMainFlow() throws IOException {
		SCGIServer server = new SCGIServer(port, new SCGIRequestHandler() {
			@Override
			public void handle(SCGIMessage request, OutputStream response) {
				if (request.isBodyAvailable()) {
					String name = new String(request.getBody());
					try {
						response.write(("hello, " + name).getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		SCGIClient client = new SCGIClient("localhost", port);
		
		Map<String, String> header = new HashMap<>();
		String content = "world";
		header.put("CONTENT_LENGTH", content.length() + "");
		
		String res = new String(client.sendAndReceiveAsByteArray(new SCGIMessage(header, content.getBytes())));
		
		assertNotNull(res);
		assertEquals("hello, world", res);
		
		server.stop();
	}

}
