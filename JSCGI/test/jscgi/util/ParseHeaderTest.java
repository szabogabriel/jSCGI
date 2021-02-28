package jscgi.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ParseHeaderTest {
	
	@Test
	void parseKeyValueHeader() throws IOException {
		String key1 = "KEY1";
		String val1 = "VAL1";
		String key2 = "KEY2";
		String val2 = "VAL2";
		String key3 = "KEY3";
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(key1.getBytes());
		os.write(0);
		os.write(val1.getBytes());
		os.write(0);
		os.write(key2.getBytes());
		os.write(0);
		os.write(val2.getBytes());
		os.write(0);
		os.write(key3.getBytes());
		os.write(0);
		os.write(0);
		
		byte[] data = os.toByteArray();
		
		Map<String, String> ret = SCGIUtil.parseHeaders(data);
		
		assertTrue(ret.containsKey(key1));
		assertEquals(val1, ret.get(key1));
		assertTrue(ret.containsKey(key2));
		assertEquals(val2, ret.get(key2));
		assertTrue(ret.containsKey(key3));
		assertEquals("", ret.get(key3));
	}

	@Test
	void createHeader() {
		String key = "KEY";
		String val = "VAL";
		
		byte [] data = new byte [] {'K', 'E', 'Y', 0, 'V', 'A', 'L', 0};
		
		Map<String, String> headers = new HashMap<>();
		
		headers.put(key, val);
		
		byte[] ret = SCGIUtil.createHeaders(headers);
		
		assertEquals(data.length, ret.length);
		for (int i = 0; i < 8; i++) {
			assertEquals(data[i], ret[i]);
		}
	}
}
