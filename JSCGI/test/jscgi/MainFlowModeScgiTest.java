package jscgi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.jscgi.Mode;
import com.jscgi.SCGIMessage;
import com.jscgi.client.SCGIClient;
import com.jscgi.server.SCGIServer;

public class MainFlowModeScgiTest {
	
	int port = 65000;
	
	@Test
	void testMainFlow() throws IOException {
		SCGIServer server = new SCGIServer(port, (req, res, mode) -> {
				if (req.isBodyAvailable()) {
					String name = new String(req.getBody());
					try {
						SCGIMessage ret = new SCGIMessage();
						ret.setBody(("hello, " + name).getBytes());
						ret.serialize(res);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}, Mode.SCGI_MESSAGE_BASED);
		
		SCGIClient client = new SCGIClient("localhost", port, Mode.SCGI_MESSAGE_BASED);
		SCGIMessage res = client.sendAndReceiveAsScgiMessage(new SCGIMessage(new HashMap<>(), "world".getBytes()));
		
		assertNotNull(res);
		assertEquals("hello, world", new String(res.getBody()));
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			res = client.sendAndReceiveAsScgiMessage(new SCGIMessage(new HashMap<>(), "world".getBytes()));
			assertEquals("hello, world", new String(res.getBody()));
		}
		long stop = System.currentTimeMillis();
		
		System.out.println("10000 calls in SCGI Mode: " + (stop - start) + "ms.");
		
		server.stop();
	}

}
