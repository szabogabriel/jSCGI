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
		SCGIServer server = new SCGIServer(port, (req, res) -> {
				if (req.isBodyAvailable()) {
					String name = new String(req.getBody());
					try {
						res.write(("hello, " + name).getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		});
		
		SCGIClient client = new SCGIClient("localhost", port);
		String res = new String(client.sendAndReceiveAsByteArray(new SCGIMessage(new HashMap<>(), "world".getBytes())));
		
		assertNotNull(res);
		assertEquals("hello, world", res);
		
		server.stop();
	}

}
