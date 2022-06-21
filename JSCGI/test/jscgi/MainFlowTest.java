package jscgi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.jscgi.SCGIMessage;
import com.jscgi.client.SCGIClient;
import com.jscgi.server.SCGIServer;

public class MainFlowTest {
	
	int port = 65000;
	
	@Test
	void testMainFlow() throws IOException {
		SCGIServer server = new SCGIServer(port, (req, res, mode) -> {
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
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			res = new String(client.sendAndReceiveAsByteArray(new SCGIMessage(new HashMap<>(), "world".getBytes())));
			assertEquals("hello, world", res);
		}
		long stop = System.currentTimeMillis();
		
		System.out.println("10000 calls in SCGI Mode: " + (stop - start) + "ms.");
		
		server.stop();
	}

}
