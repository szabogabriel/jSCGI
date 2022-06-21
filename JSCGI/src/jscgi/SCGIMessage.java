package jscgi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import jscgi.util.SCGIUtil;

public class SCGIMessage {
	
	private static final String CONTENT_LENGTH = "CONTENT_LENGTH";
	
	private Map<String, String> headers;
	private int bodyLengthInt = 0;
	private byte[] body;
	private InputStream socketIn;
	
	public SCGIMessage() {
		this(new HashMap<>(), new byte [] {});
	}
	
	public SCGIMessage(Map<String, String> headers, byte[] body) {
		if (headers == null || body == null) throw new NullPointerException();
		
		setHeaders(headers);
		setBody(body);
	}
	
	public SCGIMessage(InputStream in) throws IOException {
		this();
		socketIn = in;
		
		int contentLength = contentLength();
		byte[] headersStream = read(contentLength);
		int comma = socketIn.read();
		
		if (comma == ',') {
			headers = SCGIUtil.parseHeaders(headersStream);
			
			String bodyLength = headers.get(CONTENT_LENGTH);
			if (bodyLength != null && bodyLength.length() > 0) {
				bodyLengthInt = Integer.parseInt(bodyLength);
			}
		}
	}
	
	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
	
	public void removeHeader(String key) {
		headers.remove(key);
	}
	
	public String getHeader(String key) {
		return headers.get(key);
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public void setBody(byte[] body) {
		if (body != null) {
			this.body = body;
		}
	}
	
	public byte[] getBody() {
		byte[] ret = {};
		
		try {
			ret = read(bodyLengthInt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public InputStream getBodyStream() {
		return socketIn;
	}
	
	public boolean isBodyAvailable() {
		return bodyLengthInt > 0;
	}
	
	public int getBodySize() {
		return bodyLengthInt;
	}
	
	public void serialize(OutputStream out) throws IOException {
		headers.put(CONTENT_LENGTH, "" + body.length);
		byte[] headerData = SCGIUtil.createHeaders(headers);
		byte[] length = Integer.toString(headerData.length).getBytes();
		
		out.write(length);
		out.write(58); // the character ':'
		out.write(headerData);
		out.write(44); // the character ','
		out.write(body);
		
	}
	
	private byte[] read(int length) throws IOException {
		byte[] buffer = new byte[length];
		socketIn.read(buffer, 0, length);
		return buffer;
	}
	
	private int contentLength() throws IOException {
		int ret = 0;
		int buf;
		
		while ((buf = socketIn.read()) != ':') {
			ret = (ret * 10) + (buf - '0');
		}
		
		return ret;
	}

}
